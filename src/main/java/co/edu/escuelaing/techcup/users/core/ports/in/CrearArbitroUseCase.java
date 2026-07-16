package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;

/**
 * Creación de un usuario árbitro por un administrador (TC-04), con
 * contraseña temporal generada por el sistema.
 */
public interface CrearArbitroUseCase {
    Usuario crearArbitro(String nombreCompleto, String correo,
                          TipoIdentificacion tipoIdentificacion, String numeroIdentificacion);
}
