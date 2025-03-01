package it.project.profumeria_note.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.project.profumeria_note.dtos.LoginRequestDTO;
import it.project.profumeria_note.dtos.RegisterRequestDTO;
import it.project.profumeria_note.dtos.UserDTO;
import it.project.profumeria_note.entities.Role;
import it.project.profumeria_note.entities.User;
import it.project.profumeria_note.repositories.RoleRepository;
import it.project.profumeria_note.repositories.UserRepository;
import it.project.profumeria_note.utils.JwtUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public User register(RegisterRequestDTO registerRequest) {
		if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}

		if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new RuntimeException("Email already in use");
		}

		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail()); // ✅ Salva la mail
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		Set<Role> roles = new HashSet<>();

		if (userRepository.count() == 0) {
			Role superAdminRole = roleRepository.findByName("ROLE_SUPER_ADMIN")
					.orElseThrow(() -> new RuntimeException("Role 'ROLE_SUPER_ADMIN' not found"));
			roles.add(superAdminRole);
		} else {
			Role userRole = roleRepository.findByName("ROLE_USER")
					.orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found"));
			roles.add(userRole);
		}

		user.setRoles(roles);
		return userRepository.save(user);
	}

	public String login(LoginRequestDTO loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		// 🔹 Estrai i ruoli dell'utente e li converti in una lista di stringhe
		List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

		// 🔹 Genera il token JWT con i ruoli inclusi
		return jwtUtil.generateToken(user.getUsername(), roles);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User updateUserRoles(String username, Set<String> roleNames) {
		User userToUpdate = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("❌ Utente non trovato: " + username));

		// 🔍 Recupera l'utente autenticato (chi sta facendo la richiesta)
		String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		User authenticatedUser = userRepository.findByUsername(authenticatedUsername)
				.orElseThrow(() -> new RuntimeException("❌ Utente autenticato non trovato"));

		// 🔍 Controllo: solo SUPER_ADMIN può modificare i ruoli di un ADMIN o
		// SUPER_ADMIN
		if (userToUpdate.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"))) {
			if (!authenticatedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"))) {
				throw new RuntimeException("❌ Solo un SUPER_ADMIN può modificare un altro SUPER_ADMIN");
			}
		}

		if (userToUpdate.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
			if (!authenticatedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"))) {
				throw new RuntimeException("❌ Solo un SUPER_ADMIN può modificare un ADMIN");
			}
		}

		// 📌 Converte i nomi dei ruoli in entità Role
		Set<Role> newRoles = roleNames.stream()
				.map(roleName -> roleRepository.findByName(roleName)
						.orElseThrow(() -> new RuntimeException("❌ Ruolo non trovato: " + roleName)))
				.collect(Collectors.toSet());

		// 📌 Controllo: nessuno può rimuovere l'ultimo SUPER_ADMIN
		boolean removingLastSuperAdmin = userToUpdate.getRoles().stream()
				.anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"))
				&& newRoles.stream().noneMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"));

		if (removingLastSuperAdmin && roleRepository.countByName("ROLE_SUPER_ADMIN") == 1) {
			throw new RuntimeException("❌ Non puoi rimuovere l'ultimo SUPER_ADMIN!");
		}

		userToUpdate.setRoles(newRoles);
		return userRepository.save(userToUpdate);
	}

	// 📌 Metodo per ottenere tutti gli utenti con i loro ruoli
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream()
				.map(user -> new UserDTO(user.getUsername(),
						user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())))
				.collect(Collectors.toList());

	}
	
	public void deleteUser(String username) {
	    User userToDelete = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("❌ Utente non trovato: " + username));

	    // 📌 Verifica che l'utente autenticato abbia i permessi per eliminare
	    String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    User authenticatedUser = userRepository.findByUsername(authenticatedUsername)
	            .orElseThrow(() -> new RuntimeException("❌ Utente autenticato non trovato"));

	    // 🚨 Un ADMIN non può eliminare un altro ADMIN o un SUPER_ADMIN
	    if (userToDelete.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_SUPER_ADMIN"))) {
	        throw new RuntimeException("❌ Non puoi eliminare un SUPER_ADMIN!");
	    }
	    if (userToDelete.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")) &&
	        authenticatedUser.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_SUPER_ADMIN"))) {
	        throw new RuntimeException("❌ Solo un SUPER_ADMIN può eliminare un ADMIN!");
	    }

	    userRepository.delete(userToDelete);
	}

}
