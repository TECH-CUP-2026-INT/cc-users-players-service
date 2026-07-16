package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.DeshabilitarUsuarioUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Deshabilita la cuenta de un usuario a nivel de plataforma (TC-19),
 * validando que no esté ya inactivo ni inscrito en un torneo activo, y
 * sincronizando el estado con Identity Service.
 */
@Service
@RequiredArgsConstructor
public class DeshabilitarUsuarioService implements DeshabilitarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final IdentityCredentialsPort identityCredentialsPort;
    private final TournamentEligibilityPort tournamentEligibilityPort;

    @Override
    public Usuario deshabilitarUsuario(UUID userId, String motivo) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (usuario.getEstado() == AccountStatus.INACTIVE) {
            throw new ConflictException("El usuario ya está deshabilitado");
        }
        if (tournamentEligibilityPort.tieneTorneoActivo(userId)) {
            throw new ConflictException(
                    "No se puede deshabilitar: el usuario está inscrito en un torneo activo o en curso");
        }

        AccountStatus estadoAnterior = usuario.getEstado();
        usuario.setEstado(AccountStatus.INACTIVE);
        usuario.setMotivoDeshabilitacion(motivo);
        usuario.setFechaActualizacion(LocalDateTime.now());
        Usuario actualizado = usuarioRepository.save(usuario);

        try {
            identityCredentialsPort.actualizarEstado(actualizado.getId().toString(), AccountStatus.INACTIVE);
        } catch (IdentityIntegrationException e) {
            actualizado.setEstado(estadoAnterior);
            usuarioRepository.save(actualizado);
            throw e;
        }

        return actualizado;
    }
}
