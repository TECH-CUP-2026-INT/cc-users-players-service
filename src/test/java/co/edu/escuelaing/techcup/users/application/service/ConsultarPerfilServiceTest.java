package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarPerfilServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private ConsultarPerfilService service;

    @Test
    void retornaElUsuarioSiExiste() {
        service = new ConsultarPerfilService(usuarioRepository);
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        assertThat(service.consultarPerfil(id)).isEqualTo(usuario);
    }

    @Test
    void lanzaNotFoundSiNoExiste() {
        service = new ConsultarPerfilService(usuarioRepository);
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.consultarPerfil(id)).isInstanceOf(NotFoundException.class);
    }
}
