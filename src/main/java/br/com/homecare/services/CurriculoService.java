package br.com.homecare.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.homecare.core.exceptions.custom.RequestErrorException;
import br.com.homecare.models.entities.Curriculo;
import br.com.homecare.repositories.CurriculoRepository;
import br.com.homecare.utils.messages.ExceptionMessages;

@Service
@Transactional(rollbackFor = RequestErrorException.class)
public class CurriculoService {

	@Autowired
	private CurriculoRepository repo;

	@Autowired
	private IdiomaService idiomaService;

	@Autowired
	private EducacaoService educacaoService;

	@Autowired
	private ExperienciaService experienciaService;

	public Optional<Curriculo> find(final Long id) {
		return this.repo.findById(id);
	}

	public List<Curriculo> getAll() {
		return this.repo.findAll();
	}

	public List<Curriculo> saveAll(List<Curriculo> curriculos) {
		List<Curriculo> curr = new ArrayList<Curriculo>();
		for (Curriculo curriculo : curriculos) {
			curr.add(this.save(curriculo));
		}

		return curr;
	}

	public Curriculo save(Curriculo entity) {
		try {
			this.idiomaService.saveAll(entity.getIdiomas());
			this.educacaoService.saveAll(entity.getEducacoes());
			this.experienciaService.saveAll(entity.getExperiencias());
			
			return this.repo.save(entity);
		} catch (Exception e) {
			throw new RequestErrorException(e.getMessage());
		}
	}

	public Curriculo update(Curriculo entity) {
		if (entity.getId() == null) {
			throw new RequestErrorException(ExceptionMessages.campoNulo("id"));
		}

		Optional<Curriculo> objeto = this.find(entity.getId());
		if (objeto.isPresent()) {
			return this.repo.save(entity);
		} else {
			throw new RequestErrorException(ExceptionMessages.objetoNaoEncontrado("Curriculo"));
		}
	}

	public void delete(final Long id) {
		if (id == null) {
			throw new RequestErrorException(ExceptionMessages.campoNulo("id"));
		}

		Optional<Curriculo> objeto = this.repo.findById(id);
		if (objeto.isPresent()) {
			this.repo.delete(objeto.get());
		} else {
			throw new RequestErrorException(ExceptionMessages.objetoNaoEncontrado("Curriculo"));
		}
	}

}
