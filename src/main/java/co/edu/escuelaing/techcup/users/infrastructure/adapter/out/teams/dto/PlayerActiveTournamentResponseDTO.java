package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto;

import java.util.UUID;

public record PlayerActiveTournamentResponseDTO(UUID playerId, boolean hasActiveTournament) {
}
