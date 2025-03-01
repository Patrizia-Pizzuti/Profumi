package it.project.profumeria_note.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // ✅ Permetti richieste da Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Metodi permessi
                        .allowedHeaders("*");
            }
        };
    }
}
