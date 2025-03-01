package it.project.profumeria_note.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "note")
public class Nota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "nomeNota", nullable = false, unique = true)
	private String nome;

	@OneToMany(mappedBy = "nota", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private Set<ProfumoNota> profumi;

	public Nota() {
	}

	public Nota(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}