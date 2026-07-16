package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroOrchestratorTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private IdentityCredentialsPort identityCredentialsPort;

    private RegistroOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new RegistroOrchestrator(usuarioRepository, identityCredentialsPort, new BCryptPasswordEncoder(4));
    }

    private Usuario usuarioDePrueba() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Ada Lovelace");
        usuario.setCorreo("ada@guest.com");
        usuario.setTipoIdentificacion("CC");
        usuario.setNumeroIdentificacion("123456");
        usuario.setTipoUsuario(UserType.GUEST);
        return usuario;
    }

    @Test
    void guardaLocalmenteYCreaCredencialesEnIdentity() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(usuarioRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = orchestrator.registrar(usuarioDePrueba(), "Password123!");

        assertThat(resultado.getContrasenaHash()).isNotBlank();
        verify(identityCredentialsPort).crearCredenciales(
                eq(resultado.getId().toString()), eq("ada@guest.com"), eq("Password123!"),
                eq("Ada Lovelace"), eq(UserType.GUEST), eq(UserRole.PLAYER));
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void rechazaCorreoDuplicado() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(true);

        assertThatThrownBy(() -> orchestrator.registrar(usuarioDePrueba(), "Password123!"))
                .isInstanceOf(ConflictException.class);

        verifyNoInteractions(identityCredentialsPort);
    }

    @Test
    void rechazaNumeroIdentificacionDuplicado() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(usuarioRepository.existsByNumeroIdentificacion(anyString())).thenReturn(true);

        assertThatThrownBy(() -> orchestrator.registrar(usuarioDePrueba(), "Password123!"))
                .isInstanceOf(ConflictException.class);

        verifyNoInteractions(identityCredentialsPort);
    }

    @Test
    void compensaBorrandoElPerfilLocalSiIdentityFalla() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(usuarioRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        doThrow(new IdentityIntegrationException("Identity no disponible", new RuntimeException()))
                .when(identityCredentialsPort).crearCredenciales(anyString(), anyString(), anyString(), anyString(), any(), any());

        Usuario usuario = usuarioDePrueba();

        assertThatThrownBy(() -> orchestrator.registrar(usuario, "Password123!"))
                .isInstanceOf(IdentityIntegrationException.class);

        verify(usuarioRepository).deleteById(usuario.getId());
    }
}
