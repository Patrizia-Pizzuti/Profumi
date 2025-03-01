package it.project.profumeria_note.services;

import it.project.profumeria_note.entities.Role;
import it.project.profumeria_note.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (!roleRepository.existsByName("ROLE_SUPER_ADMIN")) {
            Role superAdmin = new Role("ROLE_SUPER_ADMIN", Set.of("MANAGE_USERS", "MANAGE_PROFUMI", "DELETE_ADMIN"));
            roleRepository.save(superAdmin);
        }
        
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role admin = new Role("ROLE_ADMIN", Set.of("MANAGE_PROFUMI", "DELETE_USERS"));
            roleRepository.save(admin);
        }
        
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role user = new Role("ROLE_USER", Set.of("VIEW_PROFUMI"));
            roleRepository.save(user);
        }
    }
}
