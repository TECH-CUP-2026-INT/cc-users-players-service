package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;

import java.util.UUID;

/**
 * Edición del perfil propio del usuario autenticado (TC-14).
 */
public interface EditarPerfilUseCase {
    Usuario editarPerfil(UUID userId, String nombreCompleto, String programaAcademico, Integer semestre);
}
