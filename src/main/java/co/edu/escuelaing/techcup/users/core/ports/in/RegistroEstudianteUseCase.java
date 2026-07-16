package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;

/**
 * Registro de un usuario estudiante (TC-01). El correo debe pertenecer a un
 * dominio institucional permitido.
 */
public interface RegistroEstudianteUseCase {
    Usuario registrarEstudiante(String nombreCompleto, String correo, String contrasena,
                                String programaAcademico, Integer semestre,
                                TipoIdentificacion tipoIdentificacion, String numeroIdentificacion);
}
