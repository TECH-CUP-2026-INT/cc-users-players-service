package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.tournament;

import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.TeamsFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Consulta a Teams Service si el jugador tiene un torneo activo (Teams, a su
 * vez, resuelve el equipo del jugador y le pregunta a Tournament Service).
 * Tournament Service no expone hoy el endpoint que Teams necesita para
 * responder esto con certeza, así que toda la cadena falla abierto (asume
 * "sin torneo activo") ante cualquier error — nunca bloquea por una
 * integración caída. El día que Tournament reconstruya su endpoint, esta
 * clase empieza a reportar el estado real sin ningún cambio de código aquí.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TournamentEligibilityAdapter implements TournamentEligibilityPort {

    private final TeamsFeignClient teamsFeignClient;

    @Override
    public boolean tieneTorneoActivo(UUID userId) {
        try {
            return teamsFeignClient.getActiveTournamentStatus(userId).hasActiveTournament();
        } catch (FeignException e) {
            log.warn("No se pudo consultar el estado de torneo activo del jugador {} en Teams Service; " +
                    "se asume sin torneo activo. Causa: {}", userId, e.getMessage());
            return false;
        }
    }
}
