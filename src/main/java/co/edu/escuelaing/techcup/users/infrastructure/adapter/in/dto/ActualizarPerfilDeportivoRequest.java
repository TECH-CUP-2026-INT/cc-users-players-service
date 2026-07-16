package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ActualizarPerfilDeportivoRequest {
    @Schema(description = "Posición de juego (opcional)", example = "FORWARD")
    private PosicionJuego posicionJuego;

    @Schema(description = "Número de camiseta, único dentro del equipo (opcional)", example = "9")
    @Min(1)
    private Integer numeroCamiseta;
}
