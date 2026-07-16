package co.edu.escuelaing.techcup.users.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI techCupUsersOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup — Users & Players Service API")
                        .description("""
                                API REST del microservicio de Usuarios y Jugadores de la plataforma TechCup.

                                Es la fuente de verdad de identidad de todos los usuarios de la plataforma
                                (estudiantes, invitados, egresados, árbitros, administradores y organizadores).
                                Identity Service solo almacena credenciales de autenticación referenciando el
                                `id` generado aquí.

                                **Convenciones**
                                - Los `id` de recurso son siempre UUID (RFC 4122).
                                - Las rutas bajo `/usuarios/admin/**` requieren rol `ADMIN` (JWT validado de forma
                                  remota contra Identity Service, sin secreto local).
                                - Las rutas bajo `/internal/**` son de uso exclusivo entre microservicios (hoy
                                  consumidas por Teams Service) y no requieren token.
                                - Los códigos `4xx` indican error de validación o de regla de negocio; `502`
                                  indica que una integración externa (Identity Service, SMTP) no respondió.

                                **Dependencias externas**
                                - `identity-service` — creación y sincronización de credenciales, validación de JWT.
                                """)
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("William S. Ruiz")
                                .email("william.ruiz@mail.escuelaing.edu.co"))
                        .license(new License()
                                .name("Internal academic use")))
                .servers(List.of(
                        new Server().url("http://localhost:8084/api/v1").description("Local development")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT emitido por Identity Service tras el login + OTP")));
    }
}
