package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.tournament;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.TeamsFeignClient;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.teams.dto.PlayerActiveTournamentResponseDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentEligibilityAdapterTest {

    @Mock
    private TeamsFeignClient teamsFeignClient;

    private TournamentEligibilityAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TournamentEligibilityAdapter(teamsFeignClient);
    }

    @Test
    void retornaTrueSiTeamsReportaTorneoActivo() {
        UUID userId = UUID.randomUUID();
        when(teamsFeignClient.getActiveTournamentStatus(userId))
                .thenReturn(new PlayerActiveTournamentResponseDTO(userId, true));

        assertThat(adapter.tieneTorneoActivo(userId)).isTrue();
    }

    @Test
    void retornaFalseSiTeamsReportaSinTorneoActivo() {
        UUID userId = UUID.randomUUID();
        when(teamsFeignClient.getActiveTournamentStatus(userId))
                .thenReturn(new PlayerActiveTournamentResponseDTO(userId, false));

        assertThat(adapter.tieneTorneoActivo(userId)).isFalse();
    }

    @Test
    void fallaAbiertoSiTeamsNoResponde() {
        UUID userId = UUID.randomUUID();
        when(teamsFeignClient.getActiveTournamentStatus(userId)).thenThrow(mock(FeignException.class));

        assertThat(adapter.tieneTorneoActivo(userId)).isFalse();
    }
}
