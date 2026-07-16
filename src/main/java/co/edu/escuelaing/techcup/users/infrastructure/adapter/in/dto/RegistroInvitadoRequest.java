package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroInvitadoRequest {
    @Schema(description = "Nombre completo del invitado", example = "Invitado Externo")
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    @Schema(description = "Correo de contacto, sin restricción de dominio", example = "invitado@externo.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;

    @Schema(description = "Contraseña de acceso, mínimo 8 caracteres", example = "Password123!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @Schema(description = "Tipo de documento de identificación", example = "CC")
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @Schema(description = "Número de documento de identificación", example = "444555666")
    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(min = 4, max = 20)
    private String numeroIdentificacion;
}
