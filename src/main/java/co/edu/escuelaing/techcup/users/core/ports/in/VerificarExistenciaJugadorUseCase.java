package co.edu.escuelaing.techcup.users.core.ports.in;

import java.util.UUID;

/**
 * Verifica si existe un jugador con el id dado. Endpoint interno consumido
 * por Teams Service antes de invitarlo a un equipo.
 */
public interface VerificarExistenciaJugadorUseCase {
    boolean existe(UUID playerId);
}
