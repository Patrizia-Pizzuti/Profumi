package it.project.profumeria_note.repositories;

import it.project.profumeria_note.entities.Profumo;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProfumoRepository extends JpaRepository<Profumo, Integer> {
	@Query("SELECT p FROM Profumo p JOIN ProfumoNota pn ON p.id = pn.profumo.id WHERE pn.nota.id = :notaId")
    List<Profumo> findByNotaId(@Param("notaId") Integer notaId);
}
