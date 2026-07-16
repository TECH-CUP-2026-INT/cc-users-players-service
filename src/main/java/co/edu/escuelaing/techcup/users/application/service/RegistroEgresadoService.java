package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEgresadoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Registra un usuario egresado y dispara el envío del código OTP de
 * verificación a su correo.
 */
@Service
@RequiredArgsConstructor
public class RegistroEgresadoService implements RegistroEgresadoUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final VerificacionOTPService otpService;

    @Override
    public Usuario registrarEgresado(String nombreCompleto, String correo, String contrasena,
                                      TipoIdentificacion tipoIdentificacion, String numeroIdentificacion,
                                      String programaAcademico) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setProgramaAcademico(programaAcademico);
        usuario.setTipoUsuario(UserType.GRADUATE);

        Usuario usuarioGuardado = registroOrchestrator.registrar(usuario, contrasena);
        otpService.generarYEnviarOTP(usuarioGuardado.getId(), usuarioGuardado.getCorreo());

        return usuarioGuardado;
    }
}
