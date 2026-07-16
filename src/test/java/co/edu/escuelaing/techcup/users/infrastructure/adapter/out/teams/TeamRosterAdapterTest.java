package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams;

import co.edu.escuelaing.techcup.users.core.exception.TeamsIntegrationException;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto.TeamRosterResponseDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamRosterAdapterTest {

    @Mock
    private TeamsFeignClient teamsFeignClient;

    private TeamRosterAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TeamRosterAdapter(teamsFeignClient);
    }

    @Test
    void retornaLosIdsDeLosCompaneros() {
        UUID userId = UUID.randomUUID();
        UUID teammateId = UUID.randomUUID();
        when(teamsFeignClient.getRoster(userId))
                .thenReturn(new TeamRosterResponseDTO(UUID.randomUUID(), List.of(userId, teammateId)));

        List<UUID> resultado = adapter.obtenerCompanerosDeEquipo(userId);

        assertThat(resultado).containsExactlyInAnyOrder(userId, teammateId);
    }

    @Test
    void retornaListaVaciaSiElJugadorNoTieneEquipo() {
        UUID userId = UUID.randomUUID();
        when(teamsFeignClient.getRoster(userId)).thenReturn(new TeamRosterResponseDTO(null, List.of()));

        assertThat(adapter.obtenerCompanerosDeEquipo(userId)).isEmpty();
    }

    @Test
    void lanzaTeamsIntegrationExceptionSiTeamsFalla() {
        UUID userId = UUID.randomUUID();
        when(teamsFeignClient.getRoster(userId)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> adapter.obtenerCompanerosDeEquipo(userId))
                .isInstanceOf(TeamsIntegrationException.class);
    }
}
