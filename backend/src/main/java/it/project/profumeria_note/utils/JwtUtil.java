package it.project.profumeria_note.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_STRING = "tua_chiave_segreta_super_sicura_tua_chiave_segreta_super_sicura";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    private final JwtParser jwtParser;

    @SuppressWarnings("deprecation")
	public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY).build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

 
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object roles = claims.get("roles");
            if (roles instanceof List<?>) {
                List<?> rawList = (List<?>) roles;
                List<String> roleList = new ArrayList<>();
                for (Object role : rawList) {
                    if (role instanceof String) {
                        roleList.add((String) role);
                    }
                }
                return roleList;
            }
            return Collections.emptyList();
        });
    }

    @SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("deprecation")
	public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setClaims(Map.of("roles", roles))
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
