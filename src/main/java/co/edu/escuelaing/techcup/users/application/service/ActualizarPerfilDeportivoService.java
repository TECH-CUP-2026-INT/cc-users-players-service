package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarPerfilDeportivoUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.TeamRosterPort;
import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Actualiza el perfil deportivo del jugador autenticado (TC-17).
 */
@Service
@RequiredArgsConstructor
public class ActualizarPerfilDeportivoService implements ActualizarPerfilDeportivoUseCase {

    private static final long MAX_FOTO_BYTES = 5L * 1024 * 1024;

    private final UsuarioRepositoryPort usuarioRepository;
    private final TeamRosterPort teamRosterPort;
    private final TournamentEligibilityPort tournamentEligibilityPort;

    @Override
    public Usuario actualizarPerfilDeportivo(UUID userId, PosicionJuego posicionJuego, Integer numeroCamiseta,
                                              byte[] foto, String fotoContentType) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (tournamentEligibilityPort.tieneTorneoActivo(userId)) {
            throw new ConflictException(
                    "No se puede actualizar el perfil deportivo: el jugador está inscrito en un torneo activo o en curso");
        }

        if (foto != null && foto.length > MAX_FOTO_BYTES) {
            throw new BadRequestException("La foto de perfil no puede superar 5 MB");
        }

        if (numeroCamiseta != null && !numeroCamiseta.equals(usuario.getNumeroCamiseta())) {
            validarNumeroCamisetaUnico(userId, numeroCamiseta);
            usuario.setNumeroCamiseta(numeroCamiseta);
        }

        if (posicionJuego != null) {
            usuario.setPosicionJuego(posicionJuego);
        }
        if (foto != null) {
            usuario.setFotoPerfil(foto);
            usuario.setFotoPerfilContentType(fotoContentType);
        }
        usuario.setFechaActualizacion(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    private void validarNumeroCamisetaUnico(UUID userId, Integer numeroCamiseta) {
        List<UUID> companeros = teamRosterPort.obtenerCompanerosDeEquipo(userId);

        boolean numeroEnUso = companeros.stream()
                .filter(companeroId -> !companeroId.equals(userId))
                .map(usuarioRepository::findById)
                .flatMap(java.util.Optional::stream)
                .anyMatch(companero -> Objects.equals(companero.getNumeroCamiseta(), numeroCamiseta));

        if (numeroEnUso) {
            throw new ConflictException("El número de camiseta ya está en uso en tu equipo");
        }
    }
}
