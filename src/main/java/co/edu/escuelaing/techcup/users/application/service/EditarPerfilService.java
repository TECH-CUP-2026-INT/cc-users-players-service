package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.EditarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EditarPerfilService implements EditarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public EditarPerfilService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario editarPerfil(UUID userId, String nombreCompleto, String programaAcademico, Integer semestre) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

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
