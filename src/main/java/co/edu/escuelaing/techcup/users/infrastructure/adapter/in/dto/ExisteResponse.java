package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExisteResponse {
    @Schema(description = "true si existe un jugador con ese id", example = "true")
    private boolean exists;
}
