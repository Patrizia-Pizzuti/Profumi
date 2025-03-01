package it.project.profumeria_note.controllers;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.project.profumeria_note.dtos.AuthResponseDTO;
import it.project.profumeria_note.dtos.LoginRequestDTO;
import it.project.profumeria_note.dtos.RegisterRequestDTO;
import it.project.profumeria_note.dtos.UserDTO;
import it.project.profumeria_note.entities.Role;
import it.project.profumeria_note.entities.User;
import it.project.profumeria_note.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Permettiamo chiamate da frontend
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            User newUser = authService.register(registerRequest);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nella registrazione: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        String token = authService.login(loginRequest);
        User user = authService.getUserByUsername(loginRequest.getUsername());

        if (user == null) {
            return ResponseEntity.badRequest().body("Errore: Utente non trovato.");
        }

        List<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthResponseDTO(user.getUsername(), token, roles));
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/update-roles/{username}")
    public ResponseEntity<?> updateUserRoles(@PathVariable String username, @RequestBody List<String> roleNames) {
        try {
            User updatedUser = authService.updateUserRoles(username, new HashSet<>(roleNames));
            return ResponseEntity.ok(Map.of("message", "✅ Ruoli aggiornati con successo per: " + updatedUser.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ Errore: " + e.getMessage()));
        }
    }

    
    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            authService.deleteUser(username);
            return ResponseEntity.ok(Map.of("message", "✅ Utente eliminato con successo: " + username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ Errore: " + e.getMessage()));
        }
    }

    

}
