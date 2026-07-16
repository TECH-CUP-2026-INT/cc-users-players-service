package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroInvitadoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Registra un usuario invitado externo y dispara el envío del código OTP de
 * verificación a su correo.
 */
@Service
@RequiredArgsConstructor
public class RegistroInvitadoService implements RegistroInvitadoUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final VerificacionOTPService otpService;

    @Override
    public Usuario registrarInvitado(String nombreCompleto, String correo, String contrasena,
                                      TipoIdentificacion tipoIdentificacion, String numeroIdentificacion) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setTipoUsuario(UserType.GUEST);

        Usuario usuarioGuardado = registroOrchestrator.registrar(usuario, contrasena);
        otpService.generarYEnviarOTP(usuarioGuardado.getId(), usuarioGuardado.getCorreo());

        return usuarioGuardado;
    }
}
