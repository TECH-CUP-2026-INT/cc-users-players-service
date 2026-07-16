package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarRolUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActualizarRolService implements ActualizarRolUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final IdentityCredentialsPort identityCredentialsPort;

    public ActualizarRolService(UsuarioRepositoryPort usuarioRepository,
                                 IdentityCredentialsPort identityCredentialsPort) {
        this.usuarioRepository = usuarioRepository;
        this.identityCredentialsPort = identityCredentialsPort;
    }

    @Override
    public Usuario actualizarRol(UUID userId, UserRole nuevoRol) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserRole rolAnterior = usuario.getRol();
        usuario.setRol(nuevoRol);
        usuario.setFechaActualizacion(LocalDateTime.now());
        Usuario actualizado = usuarioRepository.save(usuario);

        try {
            identityCredentialsPort.actualizarRol(actualizado.getId().toString(), nuevoRol);
        } catch (IdentityIntegrationException e) {
            actualizado.setRol(rolAnterior);
            usuarioRepository.save(actualizado);
            throw e;
        }

        return actualizado;
    }
}
