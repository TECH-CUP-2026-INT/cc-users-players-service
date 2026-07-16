package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CapitaniaResponse {
    @Schema(description = "true si el jugador tiene rol CAPTAIN", example = "true")
    private boolean esCapitan;
}
