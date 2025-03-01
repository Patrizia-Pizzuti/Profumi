package it.project.profumeria_note.repositories;

import it.project.profumeria_note.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    
    // Metodo per controllare se un ruolo esiste
    boolean existsByName(String name);
    // Metodo per contare quanti ruoli con un certo nome esistono
    long countByName(String name);
}
