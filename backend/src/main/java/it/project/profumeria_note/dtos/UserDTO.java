package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserDTO {
    private String username;
    private Set<String> roles;

    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
