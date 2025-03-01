package it.project.profumeria_note.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "profumi", uniqueConstraints = {@UniqueConstraint(columnNames = {"nome", "marca"})})
public class Profumo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "marca")
	private String marca;
	@Column(name = "descrizione")
	private String descrizione;
	@Column(name = "formato")
	private String formato;
	@Column(name = "prezzo")
	private Double prezzo;
	@Column(nullable = true)  // ✅ Campo opzionale
    private String immagineUrl;

	@OneToMany(mappedBy = "profumo", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<ProfumoNota> note = new HashSet<>();

	public Profumo() {
	}

	public Profumo(Integer id, String nome, String marca, String descrizione, String formato, Double prezzo) {
		this.id = id;
		this.nome = nome;
		this.marca = marca;
		this.descrizione = descrizione;
		this.formato = formato;
		this.prezzo = prezzo;
	}

	public Profumo(Integer id, String nome, String marca, String descrizione, String formato, Double prezzo,
			String immagineUrl, Set<ProfumoNota> note) {
		super();
		this.id = id;
		this.nome = nome;
		this.marca = marca;
		this.descrizione = descrizione;
		this.formato = formato;
		this.prezzo = prezzo;
		this.immagineUrl = immagineUrl;
		this.note = note;
	}

	public Profumo(String nome, String marca, String descrizione, String formato, Double prezzo, String immagine) {
		super();
		this.nome = nome;
		this.marca = marca;
		this.descrizione = descrizione;
		this.formato = formato;
		this.prezzo = prezzo;
		this.immagineUrl = immagine;
	}

}
