package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;

public interface CrearAdminOrganizadorUseCase {
    Usuario crearAdminOrganizador(String nombreCompleto, String correo,
                                   String tipoIdentificacion, String numeroIdentificacion,
                                   UserRole rol);
}
