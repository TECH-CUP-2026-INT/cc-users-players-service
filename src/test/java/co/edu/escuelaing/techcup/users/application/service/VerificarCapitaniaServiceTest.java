package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificarCapitaniaServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private VerificarCapitaniaService service;

    @BeforeEach
    void setUp() {
        service = new VerificarCapitaniaService(usuarioRepository);
    }

    @Test
    void retornaTrueSiElRolEsCapitan() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setRol(UserRole.CAPTAIN);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        assertThat(service.esCapitan(id)).isTrue();
    }

    @Test
    void retornaFalseSiElRolNoEsCapitan() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setRol(UserRole.PLAYER);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        assertThat(service.esCapitan(id)).isFalse();
    }

    @Test
    void retornaFalseSiElJugadorNoExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(service.esCapitan(id)).isFalse();
    }
}
