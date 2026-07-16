package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearAdminOrganizadorUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

/**
 * Crea un usuario administrador u organizador con contraseña temporal,
 * generada por un administrador de la plataforma.
 */
@Service
@RequiredArgsConstructor
public class CrearAdminOrganizadorService implements CrearAdminOrganizadorUseCase {

    private static final Set<UserRole> ROLES_PERMITIDOS = EnumSet.of(UserRole.ADMIN, UserRole.ORGANIZER);

    private final RegistroOrchestrator registroOrchestrator;
    private final EmailSenderPort emailSender;

    @Override
    public Usuario crearAdminOrganizador(String nombreCompleto, String correo,
                                          TipoIdentificacion tipoIdentificacion, String numeroIdentificacion,
                                          UserRole rol) {
        if (!ROLES_PERMITIDOS.contains(rol)) {
            throw new BadRequestException("El rol debe ser ADMIN u ORGANIZER");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setTipoUsuario(rol == UserRole.ADMIN ? UserType.ADMIN : UserType.ORGANIZER);
        usuario.setRol(rol);

        String contrasenaTemporal = TemporaryPasswordGenerator.generar();
        Usuario usuarioGuardado = registroOrchestrator.registrar(usuario, contrasenaTemporal);
        emailSender.enviarCorreoCredencialesTemporales(usuarioGuardado.getCorreo(), contrasenaTemporal);

        return usuarioGuardado;
    }
}
