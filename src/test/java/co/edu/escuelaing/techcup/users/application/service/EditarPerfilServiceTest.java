package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditarPerfilServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private EditarPerfilService service;

    @BeforeEach
    void setUp() {
        service = new EditarPerfilService(usuarioRepository);
    }

    @Test
    void actualizaSoloLosCamposProvistos() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Nombre Original");
        usuario.setProgramaAcademico("Programa Original");
        usuario.setSemestre(3);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = service.editarPerfil(id, "Nombre Nuevo", null, null);

        assertThat(resultado.getNombreCompleto()).isEqualTo("Nombre Nuevo");
        assertThat(resultado.getProgramaAcademico()).isEqualTo("Programa Original");
        assertThat(resultado.getSemestre()).isEqualTo(3);
    }

    @Test
    void lanzaNotFoundSiElUsuarioNoExiste() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editarPerfil(id, "X", null, null))
                .isInstanceOf(NotFoundException.class);
    }
}
