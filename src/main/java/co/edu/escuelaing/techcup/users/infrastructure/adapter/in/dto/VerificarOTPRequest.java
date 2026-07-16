package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificarOTPRequest {
    @Schema(description = "Id del usuario (UUID) que recibió el código", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @Schema(description = "Código OTP de 6 dígitos recibido por correo", example = "123456")
    @NotBlank(message = "El código OTP es obligatorio")
    private String codigoOTP;
}
