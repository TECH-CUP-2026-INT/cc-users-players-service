package co.edu.escuelaing.techcup.users.core.ports.in;

import java.util.UUID;

public interface VerificarExistenciaJugadorUseCase {
    boolean existe(UUID playerId);
}
