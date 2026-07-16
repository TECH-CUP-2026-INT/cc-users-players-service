package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;

import java.util.UUID;

/**
 * Cambio de rol de un usuario (TC-18), sincronizado con Identity Service.
 */
public interface ActualizarRolUseCase {

    /**
     * @throws co.edu.escuelaing.techcup.users.core.exception.NotFoundException si el usuario no existe
     * @throws co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException
     *         si falla la sincronización con Identity Service (el rol local se revierte)
     */
    Usuario actualizarRol(UUID userId, UserRole nuevoRol);
}
