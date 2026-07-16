package co.edu.escuelaing.techcup.users.core.ports.in;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;

import java.util.UUID;

/**
 * Actualización del perfil deportivo del jugador autenticado: posición,
 * número de camiseta y foto (TC-17). Bloqueada mientras el jugador esté
 * inscrito en un torneo activo o en curso; el número de camiseta debe ser
 * único dentro de su equipo.
 */
public interface ActualizarPerfilDeportivoUseCase {

    /**
     * @param foto             bytes de la foto de perfil, o {@code null} si no se actualiza
     * @param fotoContentType  content-type de la foto (ej. {@code image/png}), requerido si {@code foto != null}
     * @throws co.edu.escuelaing.techcup.users.core.exception.NotFoundException si el usuario no existe
     * @throws co.edu.escuelaing.techcup.users.core.exception.ConflictException
     *         si el jugador tiene un torneo activo, o el número de camiseta ya está en uso en su equipo
     * @throws co.edu.escuelaing.techcup.users.core.exception.BadRequestException si la foto supera 5 MB
     */
    Usuario actualizarPerfilDeportivo(UUID userId, PosicionJuego posicionJuego, Integer numeroCamiseta,
                                       byte[] foto, String fotoContentType);
}
