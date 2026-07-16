package co.edu.escuelaing.techcup.users.infrastructure.config.security;

import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/usuarios/registro/estudiante",
            "/usuarios/registro/invitado",
            "/usuarios/registro/egresado",
            "/otp/**",
            "/internal/**"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public IdentityAuthenticationFilter identityAuthenticationFilter(IdentityTokenValidationPort identityTokenValidationPort) {
        return new IdentityAuthenticationFilter(identityTokenValidationPort);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, IdentityAuthenticationFilter identityAuthenticationFilter,
                                            ObjectMapper objectMapper) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/usuarios/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(identityAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, ex) ->
                        writeJsonError(response, 401, "Se requiere un token válido", objectMapper))
                .accessDeniedHandler((request, response, ex) ->
                        writeJsonError(response, 403, "No tiene permisos para realizar esta acción", objectMapper))
            );
        return http.build();
    }

    private void writeJsonError(jakarta.servlet.http.HttpServletResponse response, int status, String message,
                                 ObjectMapper objectMapper) throws java.io.IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", message)));
    }
}
