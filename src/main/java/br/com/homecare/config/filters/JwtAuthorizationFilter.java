package br.com.homecare.config.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.homecare.config.security.JwtUtil;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final static String AUTHENTICATION = "Authentication";
	private final static String BEARER = "Bearer ";
	
	private UserDetailsService userDetailService;
	
	private JwtUtil jwtUtil;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailService = userDetailService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String authHeader = request.getHeader(AUTHENTICATION);
		if(authHeader != null && authHeader.startsWith(BEARER)) {
			//remover bearer
			String tokenNoBearer = authHeader.replaceAll(BEARER, "");
			UsernamePasswordAuthenticationToken auth = getAuthentication(request, tokenNoBearer);
			if(auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
		if(jwtUtil.isTokenValid(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails userDetail = this.userDetailService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
		}
		
		return null;
	}
	
}
