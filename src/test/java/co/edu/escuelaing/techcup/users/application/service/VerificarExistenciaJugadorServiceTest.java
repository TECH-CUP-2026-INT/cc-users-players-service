package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
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
class VerificarExistenciaJugadorServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private VerificarExistenciaJugadorService service;

    @BeforeEach
    void setUp() {
        service = new VerificarExistenciaJugadorService(usuarioRepository);
    }

    @Test
    void retornaTrueSiElJugadorExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(new Usuario()));

        assertThat(service.existe(id)).isTrue();
    }

    @Test
    void retornaFalseSiElJugadorNoExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(service.existe(id)).isFalse();
    }
}
