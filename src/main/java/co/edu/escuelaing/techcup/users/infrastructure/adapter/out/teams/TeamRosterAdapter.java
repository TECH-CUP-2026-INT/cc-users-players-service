package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams;

import co.edu.escuelaing.techcup.users.core.exception.TeamsIntegrationException;
import co.edu.escuelaing.techcup.users.core.ports.out.TeamRosterPort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TeamRosterAdapter implements TeamRosterPort {

    private final TeamsFeignClient teamsFeignClient;

    @Override
    public List<UUID> obtenerCompanerosDeEquipo(UUID userId) {
        try {
            var response = teamsFeignClient.getRoster(userId);
            return response.memberIds() != null ? response.memberIds() : List.of();
        } catch (FeignException e) {
            throw new TeamsIntegrationException("No se pudo consultar el equipo del jugador en Teams Service", e);
        }
    }
}
