package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.EditarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Edita los campos permitidos del perfil propio del usuario autenticado
 * (TC-16). No modifica correo ni contraseña (eso es TC-09).
 */
@Service
@RequiredArgsConstructor
public class EditarPerfilService implements EditarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final TournamentEligibilityPort tournamentEligibilityPort;

    @Override
    public Usuario editarPerfil(UUID userId, String nombreCompleto, String programaAcademico, Integer semestre) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (tournamentEligibilityPort.tieneTorneoActivo(userId)) {
            throw new ConflictException(
                    "No se puede editar el perfil: el usuario está inscrito en un torneo activo o en curso");
        }

        if (nombreCompleto != null) {
            usuario.setNombreCompleto(nombreCompleto);
        }
        if (programaAcademico != null) {
            usuario.setProgramaAcademico(programaAcademico);
        }
        if (semestre != null) {
            usuario.setSemestre(semestre);
        }
        usuario.setFechaActualizacion(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }
}
