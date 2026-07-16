package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarCapitaniaUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificarCapitaniaService implements VerificarCapitaniaUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public VerificarCapitaniaService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean esCapitan(UUID playerId) {
        return usuarioRepository.findById(playerId)
                .map(usuario -> usuario.getRol() == UserRole.CAPTAIN)
                .orElse(false);
    }
}
