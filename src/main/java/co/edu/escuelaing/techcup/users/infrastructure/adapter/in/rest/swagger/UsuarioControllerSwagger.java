package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest.swagger;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.ActualizarPerfilDeportivoRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.ActualizarRolRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CrearAdminOrganizadorRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CrearArbitroRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.DeshabilitarUsuarioRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.EditarPerfilRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilDeportivoResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilPublicoResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroEgresadoRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroEstudianteRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroInvitadoRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Documentación Swagger de {@code UsuarioController}, desacoplada del
 * controlador para no mezclar anotaciones de framework HTTP (Spring MVC)
 * con anotaciones de documentación (OpenAPI).
 */
@Tag(name = "Usuarios", description = "Registro, perfil y administración de usuarios de la plataforma")
public interface UsuarioControllerSwagger {

    String ERROR_VALIDACION = """
            { "error": "Datos inválidos", "detalles": { "correo": "Formato de correo inválido" } }
            """;
    String ERROR_CONFLICTO = """
            { "error": "El correo ya está registrado" }
            """;

    @Operation(summary = "Registrar estudiante (TC-01)",
            description = "Crea el perfil de un estudiante y sus credenciales en Identity Service. "
                    + "El correo debe pertenecer a uno de los dominios institucionales permitidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado, OTP enviado por correo",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o dominio de correo no permitido",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "409", description = "El correo o el número de identificación ya existen",
                    content = @Content(examples = @ExampleObject(value = ERROR_CONFLICTO)))
    })
    RegistroResponse registrarEstudiante(RegistroEstudianteRequest request);

    @Operation(summary = "Registrar invitado externo (TC-02)",
            description = "Crea el perfil de un invitado externo (sin restricción de dominio de correo) "
                    + "y sus credenciales en Identity Service.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado, OTP enviado por correo",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "409", description = "El correo o el número de identificación ya existen",
                    content = @Content(examples = @ExampleObject(value = ERROR_CONFLICTO)))
    })
    RegistroResponse registrarInvitado(RegistroInvitadoRequest request);

    @Operation(summary = "Registrar egresado (TC-03)",
            description = "Crea el perfil de un egresado y sus credenciales en Identity Service.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado, OTP enviado por correo",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "409", description = "El correo o el número de identificación ya existen",
                    content = @Content(examples = @ExampleObject(value = ERROR_CONFLICTO)))
    })
    RegistroResponse registrarEgresado(RegistroEgresadoRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear árbitro (TC-04, solo ADMIN)",
            description = "Un administrador crea el perfil de un árbitro; el sistema genera una contraseña "
                    + "temporal y la envía por correo junto con las credenciales.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Árbitro creado, credenciales temporales enviadas",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMIN", content = @Content),
            @ApiResponse(responseCode = "409", description = "El correo o el número de identificación ya existen",
                    content = @Content(examples = @ExampleObject(value = ERROR_CONFLICTO)))
    })
    RegistroResponse crearArbitro(CrearArbitroRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear admin u organizador (TC-05, solo ADMIN)",
            description = "Un administrador crea el perfil de otro administrador u organizador; el sistema "
                    + "genera una contraseña temporal y la envía por correo junto con las credenciales.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado, credenciales temporales enviadas",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o rol distinto de ADMIN/ORGANIZER",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMIN", content = @Content),
            @ApiResponse(responseCode = "409", description = "El correo o el número de identificación ya existen",
                    content = @Content(examples = @ExampleObject(value = ERROR_CONFLICTO)))
    })
    RegistroResponse crearAdminOrganizador(CrearAdminOrganizadorRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consultar perfil propio (TC-13)",
            description = "Retorna el perfil completo del usuario autenticado (identificado por el JWT).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil del usuario autenticado",
                    content = @Content(schema = @Schema(implementation = PerfilResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content)
    })
    PerfilResponse consultarPerfilPropio();

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Editar datos básicos del perfil propio (TC-16)",
            description = "Actualiza nombre completo y/o semestre del usuario autenticado. No permite modificar "
                    + "correo ni contraseña (eso es TC-09). Los campos omitidos no se modifican. Bloqueado si el "
                    + "usuario está inscrito en un torneo activo o en curso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado",
                    content = @Content(schema = @Schema(implementation = PerfilResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario está inscrito en un torneo activo o en curso",
                    content = @Content(examples = @ExampleObject(value = """
                            { "error": "No se puede editar el perfil: el usuario está inscrito en un torneo activo o en curso" }
                            """)))
    })
    PerfilResponse editarPerfilPropio(EditarPerfilRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consultar perfil público de otro jugador (TC-15)",
            description = "Retorna los datos públicos de cualquier usuario (sin correo ni información sensible).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil público del usuario",
                    content = @Content(schema = @Schema(implementation = PerfilPublicoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    PerfilPublicoResponse consultarPerfilDeOtroJugador(
            @Parameter(description = "Id del usuario", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID userId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ascender/cambiar rol de un usuario (TC-18, solo ADMIN)",
            description = "Cambia el rol del usuario localmente y sincroniza el cambio con Identity Service. "
                    + "Si la sincronización falla, el rol local se revierte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol actualizado",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content),
            @ApiResponse(responseCode = "502", description = "Identity Service no respondió; el cambio se revirtió", content = @Content)
    })
    RegistroResponse actualizarRol(
            @Parameter(description = "Id del usuario", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID userId,
            ActualizarRolRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deshabilitar usuario a nivel de plataforma (TC-19, solo ADMIN)",
            description = "Deshabilita la cuenta (bloquea toda interacción con la plataforma) y sincroniza el "
                    + "estado con Identity Service. Se bloquea si el usuario está inscrito en un torneo activo "
                    + "o en curso, o si ya está deshabilitado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario deshabilitado",
                    content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (motivo demasiado largo)",
                    content = @Content(examples = @ExampleObject(value = ERROR_VALIDACION))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMIN", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya está deshabilitado o tiene un torneo activo",
                    content = @Content(examples = @ExampleObject(value = """
                            { "error": "No se puede deshabilitar: el usuario está inscrito en un torneo activo o en curso" }
                            """))),
            @ApiResponse(responseCode = "502", description = "Identity Service no respondió; el cambio se revirtió", content = @Content)
    })
    RegistroResponse deshabilitarUsuario(
            @Parameter(description = "Id del usuario", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID userId,
            DeshabilitarUsuarioRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar perfil deportivo propio (TC-17)",
            description = "Actualiza posición de juego, número de camiseta y/o foto del jugador autenticado. "
                    + "El número de camiseta debe ser único dentro de su equipo (validado contra Teams Service). "
                    + "Bloqueado por completo si el jugador está inscrito en un torneo activo o en curso. "
                    + "Multipart: parte 'perfil' (JSON) + parte 'foto' opcional (JPG/PNG, máx. 5 MB).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil deportivo actualizado",
                    content = @Content(schema = @Schema(implementation = PerfilDeportivoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o foto mayor a 5 MB",
                    content = @Content(examples = @ExampleObject(value = """
                            { "error": "La foto de perfil no puede superar 5 MB" }
                            """))),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content),
            @ApiResponse(responseCode = "409", description = "Torneo activo, o número de camiseta ya en uso en el equipo",
                    content = @Content(examples = @ExampleObject(value = """
                            { "error": "El número de camiseta ya está en uso en tu equipo" }
                            """))),
            @ApiResponse(responseCode = "502", description = "Teams Service no respondió", content = @Content)
    })
    PerfilDeportivoResponse actualizarPerfilDeportivo(ActualizarPerfilDeportivoRequest request, MultipartFile foto)
            throws IOException;
}
