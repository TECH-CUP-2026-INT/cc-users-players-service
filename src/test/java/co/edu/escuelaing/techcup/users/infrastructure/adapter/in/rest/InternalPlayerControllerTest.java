package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarCapitaniaUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = InternalPlayerController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class InternalPlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerificarExistenciaJugadorUseCase verificarExistenciaJugadorUseCase;
    @MockBean
    private ConsultarPerfilUseCase consultarPerfilUseCase;
    @MockBean
    private VerificarCapitaniaUseCase verificarCapitaniaUseCase;

    @Test
    void indicaSiElJugadorExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(verificarExistenciaJugadorUseCase.existe(id)).thenReturn(true);

        mockMvc.perform(get("/internal/players/" + id + "/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    void retorna404SiElPerfilNoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(consultarPerfilUseCase.consultarPerfil(id)).thenThrow(new NotFoundException("Usuario no encontrado"));

        mockMvc.perform(get("/internal/players/" + id + "/profile"))
                .andExpect(status().isNotFound());
    }

    @Test
    void retornaElPerfilPublicoDelJugador() throws Exception {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombreCompleto("Jugador Uno");
        usuario.setTipoUsuario(UserType.STUDENT);
        usuario.setRol(UserRole.PLAYER);
        when(consultarPerfilUseCase.consultarPerfil(id)).thenReturn(usuario);

        mockMvc.perform(get("/internal/players/" + id + "/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Jugador Uno"));
    }

    @Test
    void indicaSiEsCapitan() throws Exception {
        UUID id = UUID.randomUUID();
        when(verificarCapitaniaUseCase.esCapitan(id)).thenReturn(true);

        mockMvc.perform(get("/internal/players/" + id + "/captaincy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.esCapitan").value(true));
    }
}
