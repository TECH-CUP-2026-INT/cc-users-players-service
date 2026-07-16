package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

public interface RegistroEstudianteUseCase {
    Usuario registrarEstudiante(String nombreCompleto, String correo, String contrasena,
                                String programaAcademico, Integer semestre,
                                String tipoIdentificacion, String numeroIdentificacion);
}
