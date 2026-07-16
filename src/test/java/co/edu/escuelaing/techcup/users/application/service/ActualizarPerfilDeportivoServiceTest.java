package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.out.TeamRosterPort;
import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarPerfilDeportivoServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private TeamRosterPort teamRosterPort;
    @Mock
    private TournamentEligibilityPort tournamentEligibilityPort;

    private ActualizarPerfilDeportivoService service;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new ActualizarPerfilDeportivoService(usuarioRepository, teamRosterPort, tournamentEligibilityPort);
    }

    private Usuario usuarioDePrueba() {
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        return usuario;
    }

    @Test
    void actualizaPosicionYNumeroDeCamisetaSinConflicto() {
        Usuario usuario = usuarioDePrueba();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(userId)).thenReturn(false);
        when(teamRosterPort.obtenerCompanerosDeEquipo(userId)).thenReturn(List.of(userId));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.actualizarPerfilDeportivo(userId, PosicionJuego.FORWARD, 9, null, null);

        assertThat(resultado.getPosicionJuego()).isEqualTo(PosicionJuego.FORWARD);
        assertThat(resultado.getNumeroCamiseta()).isEqualTo(9);
    }

    @Test
    void lanzaNotFoundSiElUsuarioNoExiste() {
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizarPerfilDeportivo(userId, PosicionJuego.FORWARD, null, null, null))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void bloqueaLaActualizacionSiTieneTorneoActivo() {
        Usuario usuario = usuarioDePrueba();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(userId)).thenReturn(true);

        assertThatThrownBy(() -> service.actualizarPerfilDeportivo(userId, PosicionJuego.FORWARD, null, null, null))
                .isInstanceOf(ConflictException.class);

        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void rechazaFotoMayorA5MB() {
        Usuario usuario = usuarioDePrueba();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(userId)).thenReturn(false);
        byte[] fotoGrande = new byte[6 * 1024 * 1024];

        assertThatThrownBy(() -> service.actualizarPerfilDeportivo(userId, null, null, fotoGrande, "image/png"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void rechazaNumeroDeCamisetaYaUsadoPorUnCompanero() {
        Usuario usuario = usuarioDePrueba();
        UUID companeroId = UUID.randomUUID();
        Usuario companero = new Usuario();
        companero.setId(companeroId);
        companero.setNumeroCamiseta(9);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(userId)).thenReturn(false);
        when(teamRosterPort.obtenerCompanerosDeEquipo(userId)).thenReturn(List.of(userId, companeroId));
        when(usuarioRepository.findById(companeroId)).thenReturn(Optional.of(companero));

        assertThatThrownBy(() -> service.actualizarPerfilDeportivo(userId, null, 9, null, null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("número de camiseta");

        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void permiteMantenerElMismoNumeroDeCamisetaPropio() {
        Usuario usuario = usuarioDePrueba();
        usuario.setNumeroCamiseta(9);
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(userId)).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.actualizarPerfilDeportivo(userId, null, 9, null, null);

        assertThat(resultado.getNumeroCamiseta()).isEqualTo(9);
        verify(teamRosterPort, never()).obtenerCompanerosDeEquipo(userId);
    }
}
