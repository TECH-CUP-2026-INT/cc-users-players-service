package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroResponse {
    @Schema(description = "Id del usuario (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String usuarioId;

    @Schema(description = "Estado de la cuenta tras la operación", example = "ACTIVE")
    private String estadoCuenta;

    @Schema(description = "Rol asignado al usuario", example = "PLAYER")
    private String rolAsignado;

    @Schema(description = "Mensaje de confirmación para el cliente",
            example = "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo.")
    private String mensaje;
}
