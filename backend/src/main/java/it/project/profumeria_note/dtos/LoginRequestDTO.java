package it.project.profumeria_note.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
private String username;
private String password;

public LoginRequestDTO() {
	super();
}


}
