package it.project.profumeria_note.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import it.project.profumeria_note.dtos.NotaDTO;
import it.project.profumeria_note.dtos.NotaTipoDTO;
import it.project.profumeria_note.dtos.ProfumoDTO;
import it.project.profumeria_note.entities.Nota;
import it.project.profumeria_note.entities.ProfumoNota;
import it.project.profumeria_note.repositories.NotaRepository;
import it.project.profumeria_note.repositories.ProfumoNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private ProfumoNotaRepository profumoNotaRepository;

    // Recupera tutte le note e le converte in DTO (accesso pubblico)
    public List<NotaDTO> getAllNote() {
        return notaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Recupera una nota per ID e la converte in DTO (accesso pubblico) 
    public Optional<NotaDTO> getNotaById(Integer id) {
        return notaRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Salva una nuova nota (solo ADMIN e SUPER_ADMIN) 
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public NotaDTO saveNota(NotaDTO notaDTO) {
        Nota nota = new Nota();
        nota.setNome(notaDTO.getNome());
        return convertToDTO(notaRepository.save(nota));
    }

 // Elimina una nota (solo ADMIN e SUPER_ADMIN) 
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteNota(Integer id) {
        System.out.println("🛑 Tentativo di eliminare la nota con ID: " + id);

        Optional<Nota> notaOptional = notaRepository.findById(id);
        
        if (notaOptional.isEmpty()) {
            System.out.println("❌ Errore: Nota non trovata con ID: " + id);
            throw new IllegalArgumentException("Nota non trovata con ID: " + id);
        }

        Nota nota = notaOptional.get();
        System.out.println("🔹 Nota trovata: " + nota.getNome());

        // Eliminare i riferimenti nella tabella intermedia ProfumiNote
        System.out.println("🔹 Eliminando i riferimenti in ProfumiNote...");
        profumoNotaRepository.deleteByNota(nota);

        // Ora che i riferimenti sono stati rimossi, possiamo eliminare la nota
        notaRepository.delete(nota);
        System.out.println("✅ Nota eliminata con successo: " + id);
    }

    // Recupera i profumi associati a una nota (accesso pubblico) 
    public List<ProfumoDTO> getProfumiByNota(Integer notaId) {
        List<ProfumoNota> associazioni = profumoNotaRepository.findByNota_Id(notaId);

        return associazioni.stream()
            .map(pn -> new ProfumoDTO(
                pn.getProfumo().getId(),
                pn.getProfumo().getNome(),
                pn.getProfumo().getMarca(),
                pn.getProfumo().getDescrizione(),
                pn.getProfumo().getFormato(),
                pn.getProfumo().getPrezzo(),
                pn.getProfumo().getImmagineUrl(),
                new ArrayList<>(List.of(new NotaTipoDTO(
                    pn.getNota().getId(),
                    pn.getNota().getNome(),
                    pn.getTipoNota().getId(),
                    pn.getTipoNota().getNome()
                )))
            ))
            .collect(Collectors.toList());
    }

    //Recupera i profumi associati a una nota e a un tipo (accesso pubblico) 
    public List<ProfumoDTO> getProfumiByNotaETipo(Integer notaId, Integer tipoNotaId) {
        List<ProfumoNota> associazioni = profumoNotaRepository.findByNota_IdAndTipoNota_Id(notaId, tipoNotaId);

        return associazioni.stream()
            .map(pn -> new ProfumoDTO(
                pn.getProfumo().getId(),
                pn.getProfumo().getNome(),
                pn.getProfumo().getMarca(),
                pn.getProfumo().getDescrizione(),
                pn.getProfumo().getFormato(),
                pn.getProfumo().getPrezzo(),
                pn.getProfumo().getImmagineUrl(),
                new ArrayList<>(List.of(new NotaTipoDTO(
                    pn.getNota().getId(),
                    pn.getNota().getNome(),
                    pn.getTipoNota().getId(),
                    pn.getTipoNota().getNome()
                )))
            ))
            .collect(Collectors.toList());
    }

    //Aggiorna una nota (solo ADMIN e SUPER_ADMIN) 
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public NotaDTO updateNota(Integer id, NotaDTO notaDetails) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nota non trovata con ID: " + id));

        nota.setNome(notaDetails.getNome());
        return convertToDTO(notaRepository.save(nota));
    }

    // Metodo per convertire Nota in NotaDTO 
    private NotaDTO convertToDTO(Nota nota) {
        return new NotaDTO(nota.getId(), nota.getNome());
    }
}
