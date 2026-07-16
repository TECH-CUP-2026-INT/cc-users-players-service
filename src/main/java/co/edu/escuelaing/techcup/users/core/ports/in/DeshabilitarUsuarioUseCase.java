package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

import java.util.UUID;

/**
 * Deshabilitación de una cuenta a nivel de plataforma (TC-19), solo por un
 * administrador. Bloquea la acción si el usuario está inscrito en un
 * torneo activo o en curso.
 */
public interface DeshabilitarUsuarioUseCase {

    /**
     * @param motivo razón opcional, registrada para fines de auditoría
     * @throws co.edu.escuelaing.techcup.users.core.exception.NotFoundException si el usuario no existe
     * @throws co.edu.escuelaing.techcup.users.core.exception.ConflictException
     *         si el usuario ya está deshabilitado o tiene un torneo activo
     */
    Usuario deshabilitarUsuario(UUID userId, String motivo);
}
