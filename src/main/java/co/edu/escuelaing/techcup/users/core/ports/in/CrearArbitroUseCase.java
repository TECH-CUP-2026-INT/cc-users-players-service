package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

public interface CrearArbitroUseCase {
    Usuario crearArbitro(String nombreCompleto, String correo,
                          String tipoIdentificacion, String numeroIdentificacion);
}
