package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDTO {
	private String username;
	private String password;
	private String email;
}
