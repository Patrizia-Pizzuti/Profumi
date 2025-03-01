package it.project.profumeria_note.configs;

import it.project.profumeria_note.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Legge l'header Authorization
        String authHeader = request.getHeader("Authorization");

        // Controlla se l'header è presente e inizia con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrarre il token dall'header
        String token = authHeader.substring(7);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Estrarre username e ruoli dal token
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token); // Ottiene solo i ruoli (es. ["ROLE_USER"])

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Convertire i ruoli in SimpleGrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Creare UserDetails con i ruoli corretti
                UserDetails userDetails = new User(username, "", authorities);

                // Creare il token di autenticazione per Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Impostare l'autenticazione nel SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            logger.error("Errore nell'elaborazione del token JWT: " + e.getMessage());
        }

        // Continua la catena dei filtri
        filterChain.doFilter(request, response);
    }
}
