package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Verifica si existe un jugador con el id dado. Consumido internamente por
 * Teams Service antes de invitarlo a un equipo.
 */
@Service
@RequiredArgsConstructor
public class VerificarExistenciaJugadorService implements VerificarExistenciaJugadorUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    @Override
    public boolean existe(UUID playerId) {
        return usuarioRepository.findById(playerId).isPresent();
    }
}
