package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificarOTPRequest {
    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El código OTP es obligatorio")
    private String codigoOTP;
}
