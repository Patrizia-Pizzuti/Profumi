package it.project.profumeria_note.controllers;

import it.project.profumeria_note.dtos.ProfumoDTO;
import it.project.profumeria_note.services.ProfumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profumi")
public class ProfumoController {

    @Autowired
    private ProfumoService profumoService;

    // Recupera tutti i profumi
    @GetMapping
    public ResponseEntity<List<ProfumoDTO>> getAllProfumi() {
        return ResponseEntity.ok(profumoService.getAllProfumi());
    }

    // Recupera un profumo per ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfumoDTO> getProfumoById(@PathVariable Integer id) {
        return profumoService.getProfumoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Recupera tutti i profumi che contengono una determinata nota
    @GetMapping("/nota/{notaId}")
    public ResponseEntity<List<ProfumoDTO>> getProfumiByNota(@PathVariable Integer notaId) {
        return ResponseEntity.ok(profumoService.getProfumiByNota(notaId));
    }

    // Crea un nuovo profumo (solo ADMIN e SUPER_ADMIN)
    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProfumoDTO> saveProfumo(@RequestBody ProfumoDTO profumoDTO) {
        return ResponseEntity.ok(profumoService.saveProfumo(profumoDTO));
    }

    //Aggiorna un profumo (solo ADMIN e SUPER_ADMIN)
    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProfumoDTO> updateProfumo(@PathVariable Integer id, @RequestBody ProfumoDTO profumoDTO) {
        return profumoService.updateProfumo(id, profumoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un profumo (solo ADMIN e SUPER_ADMIN)
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> deleteProfumo(@PathVariable Integer id) {
        if (profumoService.deleteProfumo(id)) {
            return ResponseEntity.ok("Profumo eliminato con successo.");
        }
        return ResponseEntity.status(404).body("Errore: Profumo non trovato.");
    }
}
