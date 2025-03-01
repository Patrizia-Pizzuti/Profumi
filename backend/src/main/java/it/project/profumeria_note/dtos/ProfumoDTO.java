package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProfumoDTO {
    private Integer id;
    private String nome;
    private String marca;
    private String descrizione;
    private String formato;
    private Double prezzo;
    private String immagineUrl;
    // ✅ Correzione: il nome corretto del setter deve essere `setNote`
    private List<NotaTipoDTO> note;

    public ProfumoDTO() {}

    public ProfumoDTO(Integer id, String nome, String marca, String descrizione, String formato, Double prezzo,
                      String immagineUrl, List<NotaTipoDTO> note) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.descrizione = descrizione;
        this.formato = formato;
        this.prezzo = prezzo;
        this.immagineUrl = immagineUrl;
        this.note = note;
    }
}
