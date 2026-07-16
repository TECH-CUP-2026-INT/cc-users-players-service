package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto;

import java.util.List;
import java.util.UUID;

public record TeamRosterResponseDTO(UUID teamId, List<UUID> memberIds) {
}
