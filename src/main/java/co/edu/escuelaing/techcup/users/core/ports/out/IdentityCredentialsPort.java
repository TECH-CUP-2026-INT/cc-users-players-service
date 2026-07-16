package co.edu.escuelaing.techcup.users.core.ports.out;

import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;

/**
 * Sincronización de credenciales con Identity Service (fuente de verdad de
 * autenticación; este servicio es la fuente de verdad de identidad).
 */
public interface IdentityCredentialsPort {

    /**
     * Crea las credenciales de un usuario recién registrado en este
     * servicio, usando el {@code userId} generado aquí.
     *
     * @throws co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException
     *         si Identity Service no está disponible o rechaza la solicitud
     */
    void crearCredenciales(String userId, String email, String password,
                            String fullName, UserType tipoUsuario, UserRole rol);

    /**
     * @throws co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException
     *         si Identity Service no está disponible o rechaza la solicitud
     */
    void actualizarRol(String userId, UserRole rol);

    /**
     * @throws co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException
     *         si Identity Service no está disponible o rechaza la solicitud
     */
    void actualizarEstado(String userId, AccountStatus estado);
}
