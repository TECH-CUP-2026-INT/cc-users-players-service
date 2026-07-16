package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;

import java.util.UUID;

public interface ActualizarRolUseCase {
    Usuario actualizarRol(UUID userId, UserRole nuevoRol);
}
