package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarCapitaniaUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Verifica si un jugador tiene el rol de capitán. Consumido internamente
 * por Teams Service para validar transferencias de capitanía.
 */
@Service
@RequiredArgsConstructor
public class VerificarCapitaniaService implements VerificarCapitaniaUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    @Override
    public boolean esCapitan(UUID playerId) {
        return usuarioRepository.findById(playerId)
                .map(usuario -> usuario.getRol() == UserRole.CAPTAIN)
                .orElse(false);
    }
}
