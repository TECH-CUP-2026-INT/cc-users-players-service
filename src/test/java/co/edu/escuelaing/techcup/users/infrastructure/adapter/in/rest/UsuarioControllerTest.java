package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarPerfilDeportivoUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarRolUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearAdminOrganizadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearArbitroUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.DeshabilitarUsuarioUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.EditarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEgresadoUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEstudianteUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroInvitadoUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.config.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UsuarioController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistroEstudianteUseCase registroEstudianteUseCase;
    @MockBean
    private RegistroInvitadoUseCase registroInvitadoUseCase;
    @MockBean
    private RegistroEgresadoUseCase registroEgresadoUseCase;
    @MockBean
    private CrearArbitroUseCase crearArbitroUseCase;
    @MockBean
    private CrearAdminOrganizadorUseCase crearAdminOrganizadorUseCase;
    @MockBean
    private ConsultarPerfilUseCase consultarPerfilUseCase;
    @MockBean
    private EditarPerfilUseCase editarPerfilUseCase;
    @MockBean
    private ActualizarRolUseCase actualizarRolUseCase;
    @MockBean
    private DeshabilitarUsuarioUseCase deshabilitarUsuarioUseCase;
    @MockBean
    private ActualizarPerfilDeportivoUseCase actualizarPerfilDeportivoUseCase;

    @AfterEach
    void limpiarContextoDeSeguridad() {
        SecurityContextHolder.clearContext();
    }

    private Usuario usuarioDePrueba(UserType tipo, UserRole rol) {
        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipo);
        usuario.setRol(rol);
        usuario.setTipoIdentificacion(co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion.CC);
        return usuario;
    }

    @Test
    void registrarEstudianteRetorna201() throws Exception {
        when(registroEstudianteUseCase.registrarEstudiante(any(), any(), any(), any(), anyInt(), any(), any()))
                .thenReturn(usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER));

        mockMvc.perform(post("/usuarios/registro/estudiante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Ada Lovelace",
                                  "correoInstitucional":"ada@university.edu.co",
                                  "contrasena":"Password123!",
                                  "programaAcademico":"Ingeniería",
                                  "semestre":5,
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"111222333"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rolAsignado").value("PLAYER"));
    }

    @Test
    void registrarEstudianteRechazaCamposFaltantes() throws Exception {
        mockMvc.perform(post("/usuarios/registro/estudiante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos inválidos"))
                .andExpect(jsonPath("$.detalles.nombreCompleto").exists());
    }

    @Test
    void registrarInvitadoRetorna201() throws Exception {
        when(registroInvitadoUseCase.registrarInvitado(any(), any(), any(), any(), any()))
                .thenReturn(usuarioDePrueba(UserType.GUEST, UserRole.PLAYER));

        mockMvc.perform(post("/usuarios/registro/invitado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Invitado Uno",
                                  "correo":"invitado@externo.com",
                                  "contrasena":"Password123!",
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"444555"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void registrarEgresadoRetorna201() throws Exception {
        when(registroEgresadoUseCase.registrarEgresado(any(), any(), any(), any(), any(), any()))
                .thenReturn(usuarioDePrueba(UserType.GRADUATE, UserRole.PLAYER));

        mockMvc.perform(post("/usuarios/registro/egresado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Egresado Uno",
                                  "correo":"egresado@gmail.com",
                                  "contrasena":"Password123!",
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"666777",
                                  "programaAcademico":"Ingeniería"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void crearArbitroRetorna201() throws Exception {
        when(crearArbitroUseCase.crearArbitro(any(), any(), any(), any()))
                .thenReturn(usuarioDePrueba(UserType.REFEREE, UserRole.REFEREE));

        mockMvc.perform(post("/usuarios/admin/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Juan Referí",
                                  "correo":"juan@techcup.com",
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"888999"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rolAsignado").value("REFEREE"));
    }

    @Test
    void crearAdminOrganizadorRetorna201() throws Exception {
        when(crearAdminOrganizadorUseCase.crearAdminOrganizador(any(), any(), any(), any(), eq(UserRole.ADMIN)))
                .thenReturn(usuarioDePrueba(UserType.ADMIN, UserRole.ADMIN));

        mockMvc.perform(post("/usuarios/admin/administradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Grace Hopper",
                                  "correo":"grace@techcup.com",
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"000111",
                                  "rol":"ADMIN"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rolAsignado").value("ADMIN"));
    }

    @Test
    void devuelve409CuandoElCorreoYaExiste() throws Exception {
        when(registroInvitadoUseCase.registrarInvitado(any(), any(), any(), any(), any()))
                .thenThrow(new ConflictException("El correo ya está registrado"));

        mockMvc.perform(post("/usuarios/registro/invitado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto":"Invitado Uno",
                                  "correo":"invitado@externo.com",
                                  "contrasena":"Password123!",
                                  "tipoIdentificacion":"CC",
                                  "numeroIdentificacion":"444555"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El correo ya está registrado"));
    }

    @Test
    void consultaPerfilPropioUsandoElUserIdDelToken() throws Exception {
        UUID userId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER);
        usuario.setNombreCompleto("Ada Lovelace");
        when(consultarPerfilUseCase.consultarPerfil(userId)).thenReturn(usuario);
        autenticarComo(userId);

        mockMvc.perform(get("/usuarios/perfil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"));
    }

    @Test
    void editaPerfilPropio() throws Exception {
        UUID userId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER);
        usuario.setNombreCompleto("Ada L. Actualizada");
        when(editarPerfilUseCase.editarPerfil(eq(userId), any(), any(), any())).thenReturn(usuario);
        autenticarComo(userId);

        mockMvc.perform(put("/usuarios/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreCompleto\":\"Ada L. Actualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Ada L. Actualizada"));
    }

    private void autenticarComo(UUID userId) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userId.toString(), null, List.of()));
    }

    @Test
    void consultaPerfilPublicoDeOtroJugador() throws Exception {
        UUID otroUserId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER);
        usuario.setNombreCompleto("Otro Jugador");
        when(consultarPerfilUseCase.consultarPerfil(otroUserId)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/" + otroUserId + "/perfil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Otro Jugador"))
                .andExpect(jsonPath("$.correo").doesNotExist());
    }

    @Test
    void actualizaRolComoAdmin() throws Exception {
        UUID userId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.CAPTAIN);
        when(actualizarRolUseCase.actualizarRol(userId, UserRole.CAPTAIN)).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/admin/" + userId + "/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rol\":\"CAPTAIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rolAsignado").value("CAPTAIN"));
    }

    @Test
    void deshabilitaUsuarioComoAdmin() throws Exception {
        UUID userId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER);
        usuario.setEstado(co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus.INACTIVE);
        when(deshabilitarUsuarioUseCase.deshabilitarUsuario(eq(userId), eq("Incumplimiento de normas")))
                .thenReturn(usuario);

        mockMvc.perform(put("/usuarios/admin/" + userId + "/deshabilitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\":\"Incumplimiento de normas\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoCuenta").value("INACTIVE"));
    }

    @Test
    void devuelve409SiElUsuarioEstaInscritoEnUnTorneoActivo() throws Exception {
        UUID userId = UUID.randomUUID();
        when(deshabilitarUsuarioUseCase.deshabilitarUsuario(eq(userId), any()))
                .thenThrow(new ConflictException(
                        "No se puede deshabilitar: el usuario está inscrito en un torneo activo o en curso"));

        mockMvc.perform(put("/usuarios/admin/" + userId + "/deshabilitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isConflict());
    }

    @Test
    void actualizaPerfilDeportivoPropio() throws Exception {
        UUID userId = UUID.randomUUID();
        Usuario usuario = usuarioDePrueba(UserType.STUDENT, UserRole.PLAYER);
        usuario.setId(userId);
        usuario.setPosicionJuego(PosicionJuego.FORWARD);
        usuario.setNumeroCamiseta(9);
        when(actualizarPerfilDeportivoUseCase.actualizarPerfilDeportivo(eq(userId), eq(PosicionJuego.FORWARD), eq(9), any(), any()))
                .thenReturn(usuario);
        autenticarComo(userId);

        var perfilPart = new MockMultipartFile(
                "perfil", "", MediaType.APPLICATION_JSON_VALUE,
                "{\"posicionJuego\":\"FORWARD\",\"numeroCamiseta\":9}".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/usuarios/perfil/deportivo").file(perfilPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posicionJuego").value("FORWARD"))
                .andExpect(jsonPath("$.numeroCamiseta").value(9));
    }

    @Test
    void devuelve409SiElNumeroDeCamisetaYaEstaEnUso() throws Exception {
        UUID userId = UUID.randomUUID();
        when(actualizarPerfilDeportivoUseCase.actualizarPerfilDeportivo(eq(userId), any(), eq(9), any(), any()))
                .thenThrow(new ConflictException("El número de camiseta ya está en uso en tu equipo"));
        autenticarComo(userId);

        var perfilPart = new MockMultipartFile(
                "perfil", "", MediaType.APPLICATION_JSON_VALUE,
                "{\"numeroCamiseta\":9}".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/usuarios/perfil/deportivo").file(perfilPart))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El número de camiseta ya está en uso en tu equipo"));
    }
}
