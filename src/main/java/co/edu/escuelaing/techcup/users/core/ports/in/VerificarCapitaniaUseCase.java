package co.edu.escuelaing.techcup.users.core.ports.in;

import java.util.UUID;

/**
 * Verifica si un jugador tiene el rol de capitán. Endpoint interno
 * consumido por Teams Service para validar transferencias de capitanía.
 */
public interface VerificarCapitaniaUseCase {
    boolean esCapitan(UUID playerId);
}
