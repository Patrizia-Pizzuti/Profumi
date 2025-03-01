package it.project.profumeria_note.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tipiNote")
public class TipoNota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;

	public TipoNota() {
	}

	public TipoNota(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public TipoNota (String nome) {
		this.nome = nome;
	}
}
