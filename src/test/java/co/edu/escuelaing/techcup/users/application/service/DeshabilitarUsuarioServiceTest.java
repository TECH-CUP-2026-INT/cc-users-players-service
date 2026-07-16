package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.core.ports.out.TournamentEligibilityPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeshabilitarUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private IdentityCredentialsPort identityCredentialsPort;
    @Mock
    private TournamentEligibilityPort tournamentEligibilityPort;

    private DeshabilitarUsuarioService service;

    @BeforeEach
    void setUp() {
        service = new DeshabilitarUsuarioService(usuarioRepository, identityCredentialsPort, tournamentEligibilityPort);
    }

    private Usuario usuarioActivo(UUID id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEstado(AccountStatus.ACTIVE);
        return usuario;
    }

    @Test
    void deshabilitaElUsuarioLocalmenteYEnIdentity() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(tournamentEligibilityPort.tieneTorneoActivo(id)).thenReturn(false);

        Usuario resultado = service.deshabilitarUsuario(id, "Incumplimiento de normas");

        assertThat(resultado.getEstado()).isEqualTo(AccountStatus.INACTIVE);
        assertThat(resultado.getMotivoDeshabilitacion()).isEqualTo("Incumplimiento de normas");
        verify(identityCredentialsPort).actualizarEstado(id.toString(), AccountStatus.INACTIVE);
    }

    @Test
    void lanzaNotFoundSiElUsuarioNoExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deshabilitarUsuario(id, null))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void lanzaConflictSiElUsuarioYaEstaDeshabilitado() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo(id);
        usuario.setEstado(AccountStatus.INACTIVE);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> service.deshabilitarUsuario(id, null))
                .isInstanceOf(ConflictException.class);
        verify(identityCredentialsPort, never()).actualizarEstado(any(), any());
    }

    @Test
    void bloqueaLaDeshabilitacionSiTieneTorneoActivo() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(tournamentEligibilityPort.tieneTorneoActivo(id)).thenReturn(true);

        assertThatThrownBy(() -> service.deshabilitarUsuario(id, null))
                .isInstanceOf(ConflictException.class);

        assertThat(usuario.getEstado()).isEqualTo(AccountStatus.ACTIVE);
        verify(usuarioRepository, never()).save(any());
        verify(identityCredentialsPort, never()).actualizarEstado(any(), any());
    }

    @Test
    void revierteElEstadoLocalSiIdentityFalla() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(tournamentEligibilityPort.tieneTorneoActivo(id)).thenReturn(false);
        doThrow(new IdentityIntegrationException("Identity caída", new RuntimeException()))
                .when(identityCredentialsPort).actualizarEstado(eq(id.toString()), eq(AccountStatus.INACTIVE));

        assertThatThrownBy(() -> service.deshabilitarUsuario(id, null))
                .isInstanceOf(IdentityIntegrationException.class);

        assertThat(usuario.getEstado()).isEqualTo(AccountStatus.ACTIVE);
        verify(usuarioRepository, times(2)).save(usuario);
    }
}
