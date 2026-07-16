package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto.PlayerActiveTournamentResponseDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto.TeamRosterResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "teams-service", url = "${teams.service.base-url}")
public interface TeamsFeignClient {

    @GetMapping("/teams/by-player/{playerId}/roster")
    TeamRosterResponseDTO getRoster(@PathVariable("playerId") UUID playerId);

    @GetMapping("/teams/by-player/{playerId}/active-tournament")
    PlayerActiveTournamentResponseDTO getActiveTournamentStatus(@PathVariable("playerId") UUID playerId);
}
