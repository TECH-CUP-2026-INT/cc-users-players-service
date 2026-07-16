package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;

/**
 * Registro de un usuario invitado externo (TC-02), sin restricción de
 * dominio de correo.
 */
public interface RegistroInvitadoUseCase {
    Usuario registrarInvitado(String nombreCompleto, String correo, String contrasena,
                               TipoIdentificacion tipoIdentificacion, String numeroIdentificacion);
}
