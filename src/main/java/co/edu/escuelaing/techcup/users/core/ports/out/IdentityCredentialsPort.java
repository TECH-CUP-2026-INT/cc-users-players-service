package co.edu.escuelaing.techcup.users.core.ports.out;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;

public interface IdentityCredentialsPort {
    void crearCredenciales(String userId, String email, String password,
                            String fullName, UserType tipoUsuario, UserRole rol);
    void actualizarRol(String userId, UserRole rol);
}
