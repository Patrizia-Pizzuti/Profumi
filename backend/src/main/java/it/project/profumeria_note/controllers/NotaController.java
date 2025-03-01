package it.project.profumeria_note.controllers;

import java.util.List;
import java.util.Optional;

import it.project.profumeria_note.dtos.NotaDTO;
import it.project.profumeria_note.dtos.ProfumoDTO;
import it.project.profumeria_note.services.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NotaController {

    @Autowired
    private NotaService notaService;

    // Recupera tutte le note
    @GetMapping
    public List<NotaDTO> getAllNote() {
        return notaService.getAllNote();
    }

    // Recupera una nota per ID 
    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> getNotaById(@PathVariable Integer id) {
        Optional<NotaDTO> nota = notaService.getNotaById(id);
        return nota.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crea una nuova nota (solo ADMIN e SUPER_ADMIN) 
    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotaDTO> saveNota(@RequestBody NotaDTO nota) {
        NotaDTO nuovaNota = notaService.saveNota(nota);
        return ResponseEntity.ok(nuovaNota);
    }

 // Elimina una nota
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNota(@PathVariable Integer id) {
        try {
            notaService.deleteNota(id);
            return ResponseEntity.ok("✅ Nota eliminata con successo.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("❌ " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("❌ " + e.getMessage());
        }
    }


    // Ottiene i profumi con una certa nota e tipo 
    @GetMapping("/tipo/{notaId}/{tipoNotaId}")
    public List<ProfumoDTO> getProfumiByNotaETipo(@PathVariable Integer notaId, @PathVariable Integer tipoNotaId) {
        return notaService.getProfumiByNotaETipo(notaId, tipoNotaId);
    }

    // Ottiene i profumi con una certa nota 
    @GetMapping("/profumi/{notaId}")
    public List<ProfumoDTO> getProfumiByNota(@PathVariable Integer notaId) {
        return notaService.getProfumiByNota(notaId);
    }

    // Aggiorna una nota 
    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotaDTO> updateNota(@PathVariable Integer id, @RequestBody NotaDTO notaDetails) {
        NotaDTO notaAggiornata = notaService.updateNota(id, notaDetails);
        return ResponseEntity.ok(notaAggiornata);
    }
}
