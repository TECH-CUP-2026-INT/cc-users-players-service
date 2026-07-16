package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest.swagger;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CapitaniaResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.ExisteResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilPublicoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

/**
 * Documentación Swagger de {@code InternalPlayerController}. Estos
 * endpoints son de uso exclusivo entre microservicios (hoy consumidos por
 * Teams Service) y no requieren token.
 */
@Tag(name = "Internal - Players", description = "Endpoints internos consumidos por otros microservicios (hoy, Teams Service)")
public interface InternalPlayerControllerSwagger {

    @Operation(summary = "Verificar existencia de un jugador",
            description = "Consumido por Teams Service antes de invitar a un jugador a un equipo.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación",
            content = @Content(schema = @Schema(implementation = ExisteResponse.class)))
    ExisteResponse existe(
            @Parameter(description = "Id del jugador", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID playerId);

    @Operation(summary = "Obtener perfil público de un jugador",
            description = "Consumido por Teams Service para mostrar los datos de los miembros de un equipo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil público del jugador",
                    content = @Content(schema = @Schema(implementation = PerfilPublicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "El jugador no existe", content = @Content)
    })
    PerfilPublicoResponse perfil(
            @Parameter(description = "Id del jugador", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID playerId);

    @Operation(summary = "Verificar si un jugador es capitán",
            description = "Consumido por Teams Service para validar transferencias de capitanía.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación",
            content = @Content(schema = @Schema(implementation = CapitaniaResponse.class)))
    CapitaniaResponse capitania(
            @Parameter(description = "Id del jugador", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID playerId);
}
