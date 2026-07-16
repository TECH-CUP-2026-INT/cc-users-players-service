package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarCapitaniaUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CapitaniaResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.ExisteResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilPublicoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/players")
public class InternalPlayerController {

    private final VerificarExistenciaJugadorUseCase verificarExistenciaJugadorUseCase;
    private final ConsultarPerfilUseCase consultarPerfilUseCase;
    private final VerificarCapitaniaUseCase verificarCapitaniaUseCase;

    public InternalPlayerController(VerificarExistenciaJugadorUseCase verificarExistenciaJugadorUseCase,
                                     ConsultarPerfilUseCase consultarPerfilUseCase,
                                     VerificarCapitaniaUseCase verificarCapitaniaUseCase) {
        this.verificarExistenciaJugadorUseCase = verificarExistenciaJugadorUseCase;
        this.consultarPerfilUseCase = consultarPerfilUseCase;
        this.verificarCapitaniaUseCase = verificarCapitaniaUseCase;
    }

    @GetMapping("/{playerId}/exists")
    public ExisteResponse existe(@PathVariable UUID playerId) {
        return new ExisteResponse(verificarExistenciaJugadorUseCase.existe(playerId));
    }

    @GetMapping("/{playerId}/profile")
    public PerfilPublicoResponse perfil(@PathVariable UUID playerId) {
        Usuario usuario = consultarPerfilUseCase.consultarPerfil(playerId);
        return new PerfilPublicoResponse(
                usuario.getId().toString(),
                usuario.getNombreCompleto(),
                usuario.getTipoUsuario().name(),
                usuario.getRol().name(),
                usuario.getProgramaAcademico()
        );
    }

    @GetMapping("/{playerId}/captaincy")
    public CapitaniaResponse capitania(@PathVariable UUID playerId) {
        return new CapitaniaResponse(verificarCapitaniaUseCase.esCapitan(playerId));
    }
}
