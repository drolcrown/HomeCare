package br.com.homecare.models.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.homecare.commons.AbstractEntity;
import br.com.homecare.models.dtos.AtendimentoDTO;

@Entity
public class Atendimento extends AbstractEntity<AtendimentoDTO> {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "descricao", nullable = false)
    private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj, this.id);
	}

    
}
