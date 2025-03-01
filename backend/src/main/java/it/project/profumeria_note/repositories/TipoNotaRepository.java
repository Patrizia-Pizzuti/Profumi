package it.project.profumeria_note.repositories;

import java.util.Optional;

import it.project.profumeria_note.entities.TipoNota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoNotaRepository extends JpaRepository<TipoNota, Integer> {
	Optional<TipoNota> findByNome(String nome);
}
