package co.edu.escuelaing.techcup.users.core.ports.out;

import java.util.UUID;

/**
 * Consulta si un usuario está inscrito en un torneo activo o en curso,
 * requisito para bloquear TC-16 (editar perfil), TC-17 (perfil deportivo) y
 * TC-19 (deshabilitar cuenta). La implementación ({@code TournamentEligibilityAdapter})
 * llama a Teams Service, que a su vez depende de un endpoint de Tournament
 * Service que hoy no existe — toda la cadena falla abierto (ver Javadoc del
 * adapter) hasta que Tournament lo reconstruya.
 */
public interface TournamentEligibilityPort {
    boolean tieneTorneoActivo(UUID userId);
}
