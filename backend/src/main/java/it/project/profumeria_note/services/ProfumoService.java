package it.project.profumeria_note.services;

import it.project.profumeria_note.dtos.NotaTipoDTO;
import it.project.profumeria_note.dtos.ProfumoDTO;
import it.project.profumeria_note.entities.Nota;
import it.project.profumeria_note.entities.Profumo;
import it.project.profumeria_note.entities.ProfumoNota;
import it.project.profumeria_note.entities.TipoNota;
import it.project.profumeria_note.repositories.NotaRepository;
import it.project.profumeria_note.repositories.ProfumoNotaRepository;
import it.project.profumeria_note.repositories.ProfumoRepository;
import it.project.profumeria_note.repositories.TipoNotaRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

@Transactional
@Service
public class ProfumoService {

    @Autowired
    private ProfumoRepository profumoRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TipoNotaRepository tipoNotaRepository;

    @Autowired
    private ProfumoNotaRepository profumoNotaRepository;

    // Recupera tutti i profumi con dettagli sulle note
    public List<ProfumoDTO> getAllProfumi() {
        return profumoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Recupera un profumo per ID con dettagli sulle note 
    public Optional<ProfumoDTO> getProfumoById(Integer id) {
        return profumoRepository.findById(id).map(this::convertToDTO);
    }

    // Recupera tutti i profumi che contengono una determinata nota */
    public List<ProfumoDTO> getProfumiByNota(Integer notaId) {
        List<Profumo> profumi = profumoRepository.findByNotaId(notaId);

        return profumi.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Crea un nuovo profumo con associazione alle note 
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ProfumoDTO saveProfumo(ProfumoDTO profumoDTO) {
        Profumo profumo = new Profumo(
                profumoDTO.getNome(),
                profumoDTO.getMarca(),
                profumoDTO.getDescrizione(),
                profumoDTO.getFormato(),
                profumoDTO.getPrezzo(),
                profumoDTO.getImmagineUrl()
        );

        //  Salva il profumo PRIMA di associarlo alle note
        profumo = profumoRepository.save(profumo);

        if (profumoDTO.getNote() != null && !profumoDTO.getNote().isEmpty()) {
            associaNoteAProfumo(profumo, profumoDTO.getNote());
        }

        return convertToDTO(profumo);
    }

    // Aggiorna un profumo 
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Optional<ProfumoDTO> updateProfumo(Integer id, ProfumoDTO profumoDTO) {
    	Optional<Profumo> optionalProfumo = profumoRepository.findById(id);
    	if (optionalProfumo.isEmpty()) {
    		return Optional.empty();
    	}
    	
    	return optionalProfumo.map(profumo -> {
            // 🔹 Aggiorna i campi base del Profumo
            profumo.setNome(profumoDTO.getNome());
            profumo.setMarca(profumoDTO.getMarca());
            profumo.setDescrizione(profumoDTO.getDescrizione());
            profumo.setFormato(profumoDTO.getFormato());
            profumo.setPrezzo(profumoDTO.getPrezzo());
            profumo.setImmagineUrl(profumoDTO.getImmagineUrl());

            // 🔹 Rimuove le vecchie associazioni prima di aggiungere le nuove
            profumoNotaRepository.deleteByProfumoId(profumo.getId());
            profumo.getNote().clear();  // Assicura che Hibernate rimuova le note obsolete
            
            // 🔹 Aggiunge le nuove note se presenti
            if (profumoDTO.getNote() != null && !profumoDTO.getNote().isEmpty()) {
                associaNoteAProfumo(profumo, profumoDTO.getNote());
            }

            return convertToDTO(profumoRepository.save(profumo));
        });
    }

    // Elimina un profumo 
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public boolean deleteProfumo(Integer id) {
    	Boolean profumoEsiste = profumoRepository.existsById(id);
    	
        if (profumoEsiste) {
//            profumoNotaRepository.deleteByProfumoId(id);
            profumoRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Metodo per convertire un Profumo in ProfumoDTO
    private ProfumoDTO convertToDTO(Profumo profumo) {
        List<NotaTipoDTO> noteDTOs = profumo.getNote().stream() // ❌ ERA getProfumoNote(), ORA È getNote()
                .map(pn -> new NotaTipoDTO(
                        pn.getNota().getId(), 
                        pn.getNota().getNome(), 
                        pn.getTipoNota().getId(), 
                        pn.getTipoNota().getNome()
                ))
                .collect(Collectors.toList());

        return new ProfumoDTO(
                profumo.getId(),
                profumo.getNome(),
                profumo.getMarca(),
                profumo.getDescrizione(),
                profumo.getFormato(),
                profumo.getPrezzo(),
                profumo.getImmagineUrl(),
                noteDTOs
        );
    }


    // Metodo per associare note a un profumo 
    private void associaNoteAProfumo(Profumo profumo, List<NotaTipoDTO> note) {
        if (note == null || note.isEmpty()) {
            return;
        }

        Set<ProfumoNota> noteAssociare = new HashSet<>();

        for (NotaTipoDTO notaTipoDTO : note) {
            Nota nota = notaRepository.findById(notaTipoDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Nota non trovata con ID: " + notaTipoDTO.getId()));

            TipoNota tipoNota = tipoNotaRepository.findById(notaTipoDTO.getTipoNotaId())
                    .orElseThrow(() -> new RuntimeException("TipoNota non trovata con ID: " + notaTipoDTO.getTipoNotaId()));

            ProfumoNota profumoNota = new ProfumoNota();
            profumoNota.setProfumo(profumo);
            profumoNota.setNota(nota);
            profumoNota.setTipoNota(tipoNota);
            
            noteAssociare.add(profumoNota);
        }

        profumoNotaRepository.saveAll(noteAssociare);
        profumo.setNote(noteAssociare);
    }
}
