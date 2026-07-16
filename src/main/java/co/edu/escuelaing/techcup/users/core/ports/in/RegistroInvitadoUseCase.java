package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

public interface RegistroInvitadoUseCase {
    Usuario registrarInvitado(String nombreCompleto, String correo, String contrasena,
                               String tipoIdentificacion, String numeroIdentificacion);
}
