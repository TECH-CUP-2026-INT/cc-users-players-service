package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarCapitaniaUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/players")
@Tag(name = "Internal - Players", description = "Endpoints internos consumidos por otros microservicios (hoy, Teams Service)")
@RequiredArgsConstructor
public class InternalPlayerController {

    private final VerificarExistenciaJugadorUseCase verificarExistenciaJugadorUseCase;
    private final ConsultarPerfilUseCase consultarPerfilUseCase;
    private final VerificarCapitaniaUseCase verificarCapitaniaUseCase;

    @Operation(summary = "Verificar existencia de un jugador",
            description = "Consumido por Teams Service antes de invitar a un jugador a un equipo.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación",
            content = @Content(schema = @Schema(implementation = ExisteResponse.class)))
    @GetMapping("/{playerId}/exists")
    public ExisteResponse existe(@Parameter(description = "Id del jugador") @PathVariable UUID playerId) {
        return new ExisteResponse(verificarExistenciaJugadorUseCase.existe(playerId));
    }

    @Operation(summary = "Obtener perfil público de un jugador",
            description = "Consumido por Teams Service para mostrar los datos de los miembros de un equipo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil público del jugador",
                    content = @Content(schema = @Schema(implementation = PerfilPublicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "El jugador no existe", content = @Content)
    })
    @GetMapping("/{playerId}/profile")
    public PerfilPublicoResponse perfil(@Parameter(description = "Id del jugador") @PathVariable UUID playerId) {
        Usuario usuario = consultarPerfilUseCase.consultarPerfil(playerId);
        return new PerfilPublicoResponse(
                usuario.getId().toString(),
                usuario.getNombreCompleto(),
                usuario.getTipoUsuario().name(),
                usuario.getRol().name(),
                usuario.getProgramaAcademico()
        );
    }

    @Operation(summary = "Verificar si un jugador es capitán",
            description = "Consumido por Teams Service para validar transferencias de capitanía.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación",
            content = @Content(schema = @Schema(implementation = CapitaniaResponse.class)))
    @GetMapping("/{playerId}/captaincy")
    public CapitaniaResponse capitania(@Parameter(description = "Id del jugador") @PathVariable UUID playerId) {
        return new CapitaniaResponse(verificarCapitaniaUseCase.esCapitan(playerId));
    }
}
