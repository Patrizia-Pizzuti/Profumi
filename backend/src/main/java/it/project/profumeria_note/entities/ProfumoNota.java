package it.project.profumeria_note.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "profumiENote")
public class ProfumoNota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "profumo_id")
	@JsonIgnore
	private Profumo profumo;

	@ManyToOne
	@JoinColumn(name = "nota_id")
	@JsonBackReference
	private Nota nota;

	@ManyToOne
	@JoinColumn(name = "tipo_nota_id")
	private TipoNota tipoNota;

	public ProfumoNota() {
	}

	public ProfumoNota(Integer id, Profumo profumo, Nota nota, TipoNota tipoNota) {
		this.id = id;
		this.profumo = profumo;
		this.nota = nota;
		this.tipoNota = tipoNota;
	}
	

	public ProfumoNota(Profumo profumo, Nota nota, TipoNota tipoNota) {
		super();
		this.profumo = profumo;
		this.nota = nota;
		this.tipoNota = tipoNota;
	}

}
