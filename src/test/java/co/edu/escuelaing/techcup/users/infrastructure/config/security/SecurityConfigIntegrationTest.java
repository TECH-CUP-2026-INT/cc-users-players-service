package co.edu.escuelaing.techcup.users.infrastructure.config.security;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarRolUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarExistenciaJugadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica end-to-end (con la cadena de filtros de Spring Security real,
 * no mockeada) las reglas de {@link SecurityConfig}: rutas públicas,
 * rutas que solo requieren autenticación y rutas restringidas a
 * {@code ROLE_ADMIN}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdentityTokenValidationPort identityTokenValidationPort;
    @MockBean
    private ActualizarRolUseCase actualizarRolUseCase;
    @MockBean
    private VerificarExistenciaJugadorUseCase verificarExistenciaJugadorUseCase;

    @Test
    void rutaProtegidaSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/usuarios/perfil"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rutaDeAdminSinTokenRetorna401() throws Exception {
        mockMvc.perform(put("/usuarios/admin/" + UUID.randomUUID() + "/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rol\":\"CAPTAIN\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rutaDeAdminConTokenValidoPeroRolInsuficienteRetorna403() throws Exception {
        when(identityTokenValidationPort.validar("Bearer valido-player"))
                .thenReturn(Optional.of(new IdentityTokenValidationPort.TokenInfo(
                        UUID.randomUUID().toString(), "jugador@techcup.com", "PLAYER")));

        mockMvc.perform(put("/usuarios/admin/" + UUID.randomUUID() + "/rol")
                        .header("Authorization", "Bearer valido-player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rol\":\"CAPTAIN\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rutaDeAdminConRolAdminPermiteElAcceso() throws Exception {
        UUID userId = UUID.randomUUID();
        when(identityTokenValidationPort.validar("Bearer valido-admin"))
                .thenReturn(Optional.of(new IdentityTokenValidationPort.TokenInfo(
                        UUID.randomUUID().toString(), "admin@techcup.com", "ADMIN")));
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setTipoUsuario(UserType.STUDENT);
        usuario.setRol(UserRole.CAPTAIN);
        when(actualizarRolUseCase.actualizarRol(eq(userId), any())).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/admin/" + userId + "/rol")
                        .header("Authorization", "Bearer valido-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rol\":\"CAPTAIN\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void rutaDeRegistroEsPublicaSinToken() throws Exception {
        // Cuerpo vacío: se espera 400 por validación, no 401 — prueba que la
        // ruta es pública (llegó al controlador) en vez de bloqueada por seguridad.
        mockMvc.perform(post("/usuarios/registro/estudiante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rutaInternaEsPublicaSinToken() throws Exception {
        UUID playerId = UUID.randomUUID();
        when(verificarExistenciaJugadorUseCase.existe(playerId)).thenReturn(true);

        mockMvc.perform(get("/internal/players/" + playerId + "/exists"))
                .andExpect(status().isOk());
    }
}
