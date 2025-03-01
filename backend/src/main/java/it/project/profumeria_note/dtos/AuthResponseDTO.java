package it.project.profumeria_note.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class AuthResponseDTO {
    private String username;
    private String token;
    private List<String> roles;

    public AuthResponseDTO(String username, String token, List<String> roles) {
        this.username = username;
        this.token = token;
        this.roles = roles;
    }

}
