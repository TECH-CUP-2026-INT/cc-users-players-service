package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;

/**
 * Creación de un usuario administrador u organizador por un administrador
 * (TC-05), con contraseña temporal generada por el sistema.
 */
public interface CrearAdminOrganizadorUseCase {
    Usuario crearAdminOrganizador(String nombreCompleto, String correo,
                                   TipoIdentificacion tipoIdentificacion, String numeroIdentificacion,
                                   UserRole rol);
}
