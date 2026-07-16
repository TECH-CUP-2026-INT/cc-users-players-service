package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEgresadoUseCase;
import org.springframework.stereotype.Service;

@Service
public class RegistroEgresadoService implements RegistroEgresadoUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final VerificacionOTPService otpService;

    public RegistroEgresadoService(RegistroOrchestrator registroOrchestrator,
                                    VerificacionOTPService otpService) {
        this.registroOrchestrator = registroOrchestrator;
        this.otpService = otpService;
    }

    @Override
    public Usuario registrarEgresado(String nombreCompleto, String correo, String contrasena,
                                      String tipoIdentificacion, String numeroIdentificacion,
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
