package co.edu.escuelaing.techcup.users.core.ports.out;

import java.util.List;
import java.util.UUID;

/**
 * Consulta a Teams Service el roster del equipo de un jugador, usado para
 * validar unicidad de número de camiseta dentro del equipo (TC-17).
 */
public interface TeamRosterPort {

    /**
     * @return ids de todos los miembros del equipo del jugador (incluyéndolo
     *         a él), o lista vacía si no pertenece a ningún equipo.
     * @throws co.edu.escuelaing.techcup.users.core.exception.TeamsIntegrationException
     *         si Teams Service no está disponible
     */
    List<UUID> obtenerCompanerosDeEquipo(UUID userId);
}
