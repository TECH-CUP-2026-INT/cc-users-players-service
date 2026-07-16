package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
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
class ActualizarRolServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private IdentityCredentialsPort identityCredentialsPort;

    private ActualizarRolService service;

    @BeforeEach
    void setUp() {
        service = new ActualizarRolService(usuarioRepository, identityCredentialsPort);
    }

    @Test
    void actualizaElRolLocalmenteYEnIdentity() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(UserRole.PLAYER);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.actualizarRol(id, UserRole.CAPTAIN);

        assertThat(resultado.getRol()).isEqualTo(UserRole.CAPTAIN);
        verify(identityCredentialsPort).actualizarRol(id.toString(), UserRole.CAPTAIN);
    }

    @Test
    void lanzaNotFoundSiElUsuarioNoExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizarRol(id, UserRole.CAPTAIN))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void revierteElRolLocalSiIdentityFalla() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(UserRole.PLAYER);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        doThrow(new IdentityIntegrationException("Identity caída", new RuntimeException()))
                .when(identityCredentialsPort).actualizarRol(eq(id.toString()), eq(UserRole.CAPTAIN));

        assertThatThrownBy(() -> service.actualizarRol(id, UserRole.CAPTAIN))
                .isInstanceOf(IdentityIntegrationException.class);

        assertThat(usuario.getRol()).isEqualTo(UserRole.PLAYER);
        verify(usuarioRepository, times(2)).save(usuario);
    }
}
