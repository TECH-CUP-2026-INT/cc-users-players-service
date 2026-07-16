package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeshabilitarUsuarioRequest {
    @Schema(description = "Razón de la deshabilitación, opcional pero recomendada para auditoría",
            example = "Incumplimiento del reglamento de conducta")
    @Size(max = 300, message = "El motivo no puede superar 300 caracteres")
    private String motivo;
}
