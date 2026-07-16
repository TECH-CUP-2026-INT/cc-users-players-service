package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificarExistenciaJugadorService implements VerificarExistenciaJugadorUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public VerificarExistenciaJugadorService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean existe(UUID playerId) {
        return usuarioRepository.findById(playerId).isPresent();
    }
}
