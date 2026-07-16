package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroInvitadoUseCase;
import org.springframework.stereotype.Service;

@Service
public class RegistroInvitadoService implements RegistroInvitadoUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final VerificacionOTPService otpService;

    public RegistroInvitadoService(RegistroOrchestrator registroOrchestrator,
                                    VerificacionOTPService otpService) {
        this.registroOrchestrator = registroOrchestrator;
        this.otpService = otpService;
    }

    @Override
    public Usuario registrarInvitado(String nombreCompleto, String correo, String contrasena,
                                      String tipoIdentificacion, String numeroIdentificacion) {
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
