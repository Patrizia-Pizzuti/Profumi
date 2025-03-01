package it.project.profumeria_note.repositories;

import java.util.List;

import it.project.profumeria_note.entities.Nota;
import it.project.profumeria_note.entities.ProfumoNota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface ProfumoNotaRepository extends JpaRepository<ProfumoNota, Integer> {
    List<ProfumoNota> findByNotaId(Integer id);
    List<ProfumoNota> findByNotaIdAndTipoNota_Id(Integer notaId, Integer tipoNotaId);
    List<ProfumoNota> findByProfumo_Id(Integer profumoId);
    List<ProfumoNota> findByNota_IdAndTipoNota_Id(Integer notaId, Integer tipoNotaId);
    List<ProfumoNota> findByNota_Id (Integer notaId);
    void deleteByProfumo_Id(Integer profumoId);
    void deleteByProfumoId(Integer profumoId);
    void deleteByNota(Nota nota);
    } 


   

