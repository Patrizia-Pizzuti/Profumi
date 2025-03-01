package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaTipoDTO {
    private Integer id;
    private String nomeNota;
    private Integer tipoNotaId;
    private String tipoNotaNome;
	public NotaTipoDTO(Integer id, String nomeNota, Integer tipoNotaId, String tipoNotaNome) {
		super();
		this.id = id;
		this.nomeNota = nomeNota;
		this.tipoNotaId = tipoNotaId;
		this.tipoNotaNome = tipoNotaNome;
	}
}
