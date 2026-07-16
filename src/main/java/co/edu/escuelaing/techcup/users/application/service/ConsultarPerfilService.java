package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Consulta el perfil de un usuario por su id, ya sea el propio (perfil
 * privado) o el de otro jugador (perfil público).
 */
@Service
@RequiredArgsConstructor
public class ConsultarPerfilService implements ConsultarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    @Override
    public Usuario consultarPerfil(UUID userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}
