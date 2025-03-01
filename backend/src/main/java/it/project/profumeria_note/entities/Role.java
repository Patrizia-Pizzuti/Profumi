package it.project.profumeria_note.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name; // Nome del ruolo (USER, ADMIN, SUPER_ADMIN)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission", nullable = false)
    private Set<String> permissions;

    public Role() {
        this.permissions = Set.of(); // Evita null
    }

    public Role(String name, Set<String> permissions) {
        this.name = name;
        this.permissions = (permissions != null) ? permissions : Set.of(); // Evita null
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = (permissions != null) ? permissions : Set.of();
    }
}
