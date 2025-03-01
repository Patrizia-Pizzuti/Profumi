package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotaDTO {
	private Integer id;
	private String nome;
	public NotaDTO(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
}

	