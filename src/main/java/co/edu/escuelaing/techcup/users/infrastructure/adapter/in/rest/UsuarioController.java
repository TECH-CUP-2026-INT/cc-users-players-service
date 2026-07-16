package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.ports.in.ActualizarRolUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.ConsultarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearAdminOrganizadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearArbitroUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.EditarPerfilUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEgresadoUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEstudianteUseCase;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroInvitadoUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.ActualizarRolRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CrearAdminOrganizadorRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.CrearArbitroRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.EditarPerfilRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilPublicoResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.PerfilResponse;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroEgresadoRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroEstudianteRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroInvitadoRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final RegistroEstudianteUseCase registroEstudianteUseCase;
    private final RegistroInvitadoUseCase registroInvitadoUseCase;
    private final RegistroEgresadoUseCase registroEgresadoUseCase;
    private final CrearArbitroUseCase crearArbitroUseCase;
    private final CrearAdminOrganizadorUseCase crearAdminOrganizadorUseCase;
    private final ConsultarPerfilUseCase consultarPerfilUseCase;
    private final EditarPerfilUseCase editarPerfilUseCase;
    private final ActualizarRolUseCase actualizarRolUseCase;

    public UsuarioController(RegistroEstudianteUseCase registroEstudianteUseCase,
                              RegistroInvitadoUseCase registroInvitadoUseCase,
                              RegistroEgresadoUseCase registroEgresadoUseCase,
                              CrearArbitroUseCase crearArbitroUseCase,
                              CrearAdminOrganizadorUseCase crearAdminOrganizadorUseCase,
                              ConsultarPerfilUseCase consultarPerfilUseCase,
                              EditarPerfilUseCase editarPerfilUseCase,
                              ActualizarRolUseCase actualizarRolUseCase) {
        this.registroEstudianteUseCase = registroEstudianteUseCase;
        this.registroInvitadoUseCase = registroInvitadoUseCase;
        this.registroEgresadoUseCase = registroEgresadoUseCase;
        this.crearArbitroUseCase = crearArbitroUseCase;
        this.crearAdminOrganizadorUseCase = crearAdminOrganizadorUseCase;
        this.consultarPerfilUseCase = consultarPerfilUseCase;
        this.editarPerfilUseCase = editarPerfilUseCase;
        this.actualizarRolUseCase = actualizarRolUseCase;
    }

    @PostMapping("/registro/estudiante")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse registrarEstudiante(@Valid @RequestBody RegistroEstudianteRequest request) {
        Usuario usuario = registroEstudianteUseCase.registrarEstudiante(
            request.getNombreCompleto(),
            request.getCorreoInstitucional(),
            request.getContrasena(),
            request.getProgramaAcademico(),
            request.getSemestre(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion()
        );

        return respuestaRegistro(usuario, "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo.");
    }

    @PostMapping("/registro/invitado")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse registrarInvitado(@Valid @RequestBody RegistroInvitadoRequest request) {
        Usuario usuario = registroInvitadoUseCase.registrarInvitado(
            request.getNombreCompleto(),
            request.getCorreo(),
            request.getContrasena(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion()
        );

        return respuestaRegistro(usuario, "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo.");
    }

    @PostMapping("/registro/egresado")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse registrarEgresado(@Valid @RequestBody RegistroEgresadoRequest request) {
        Usuario usuario = registroEgresadoUseCase.registrarEgresado(
            request.getNombreCompleto(),
            request.getCorreo(),
            request.getContrasena(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion(),
            request.getProgramaAcademico()
        );

        return respuestaRegistro(usuario, "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo.");
    }

    @PostMapping("/admin/arbitros")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse crearArbitro(@Valid @RequestBody CrearArbitroRequest request) {
        Usuario usuario = crearArbitroUseCase.crearArbitro(
            request.getNombreCompleto(),
            request.getCorreo(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion()
        );

        return respuestaRegistro(usuario, "Árbitro creado exitosamente. Se enviaron las credenciales temporales a su correo.");
    }

    @PostMapping("/admin/administradores")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse crearAdminOrganizador(@Valid @RequestBody CrearAdminOrganizadorRequest request) {
        Usuario usuario = crearAdminOrganizadorUseCase.crearAdminOrganizador(
            request.getNombreCompleto(),
            request.getCorreo(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion(),
            request.getRol()
        );

        return respuestaRegistro(usuario, "Usuario creado exitosamente. Se enviaron las credenciales temporales a su correo.");
    }

    @GetMapping("/perfil")
    public PerfilResponse consultarPerfilPropio() {
        Usuario usuario = consultarPerfilUseCase.consultarPerfil(usuarioIdAutenticado());
        return respuestaPerfil(usuario);
    }

    @PutMapping("/perfil")
    public PerfilResponse editarPerfilPropio(@Valid @RequestBody EditarPerfilRequest request) {
        Usuario usuario = editarPerfilUseCase.editarPerfil(
            usuarioIdAutenticado(),
            request.getNombreCompleto(),
            request.getProgramaAcademico(),
            request.getSemestre()
        );
        return respuestaPerfil(usuario);
    }

    private UUID usuarioIdAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString((String) authentication.getPrincipal());
    }

    @GetMapping("/{userId}/perfil")
    public PerfilPublicoResponse consultarPerfilDeOtroJugador(@PathVariable UUID userId) {
        Usuario usuario = consultarPerfilUseCase.consultarPerfil(userId);
        return new PerfilPublicoResponse(
            usuario.getId().toString(),
            usuario.getNombreCompleto(),
            usuario.getTipoUsuario().name(),
            usuario.getRol().name(),
            usuario.getProgramaAcademico()
        );
    }

    @PutMapping("/admin/{userId}/rol")
    public RegistroResponse actualizarRol(@PathVariable UUID userId, @Valid @RequestBody ActualizarRolRequest request) {
        Usuario usuario = actualizarRolUseCase.actualizarRol(userId, request.getRol());
        return respuestaRegistro(usuario, "Rol actualizado a " + usuario.getRol());
    }

    private RegistroResponse respuestaRegistro(Usuario usuario, String mensaje) {
        return new RegistroResponse(
            usuario.getId().toString(),
            usuario.getEstado().name(),
            usuario.getRol().name(),
            mensaje
        );
    }

    private PerfilResponse respuestaPerfil(Usuario usuario) {
        return new PerfilResponse(
            usuario.getId().toString(),
            usuario.getNombreCompleto(),
            usuario.getCorreo(),
            usuario.getTipoUsuario().name(),
            usuario.getRol().name(),
            usuario.getEstado().name(),
            usuario.getTipoIdentificacion(),
            usuario.getNumeroIdentificacion(),
            usuario.getProgramaAcademico(),
            usuario.getSemestre(),
            usuario.getVerificadoOTP(),
            usuario.getFechaRegistro()
        );
    }
}
