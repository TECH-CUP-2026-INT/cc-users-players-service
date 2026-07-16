package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilDeportivoResponse {
    @Schema(description = "Id del usuario (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String usuarioId;

    @Schema(description = "Posición de juego actual", example = "FORWARD")
    private String posicionJuego;

    @Schema(description = "Número de camiseta actual", example = "9")
    private Integer numeroCamiseta;

    @Schema(description = "true si la foto de perfil fue actualizada en esta operación", example = "true")
    private boolean fotoActualizada;

    @Schema(description = "Mensaje de confirmación para el cliente", example = "Perfil deportivo actualizado exitosamente")
    private String mensaje;
}
