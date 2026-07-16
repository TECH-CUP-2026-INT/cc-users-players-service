package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearAdminOrganizadorRequest {
    @Schema(description = "Nombre completo del administrador u organizador", example = "Grace Hopper")
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    @Schema(description = "Correo al que se enviarán las credenciales temporales", example = "grace.hopper@techcup.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;

    @Schema(description = "Tipo de documento de identificación", example = "CC")
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @Schema(description = "Número de documento de identificación", example = "000111222")
    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(min = 4, max = 20)
    private String numeroIdentificacion;

    @Schema(description = "Rol a asignar: solo ADMIN u ORGANIZER", example = "ADMIN")
    @NotNull(message = "El rol es obligatorio (ADMIN u ORGANIZER)")
    private UserRole rol;
}
