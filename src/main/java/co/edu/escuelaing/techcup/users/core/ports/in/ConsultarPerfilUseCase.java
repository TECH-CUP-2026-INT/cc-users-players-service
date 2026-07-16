package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

import java.util.UUID;

/**
 * Consulta del perfil de un usuario por id (TC-13 perfil propio, TC-15
 * perfil público de otro jugador).
 */
public interface ConsultarPerfilUseCase {
    Usuario consultarPerfil(UUID userId);
}
