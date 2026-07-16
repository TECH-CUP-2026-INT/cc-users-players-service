package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEstudianteUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class RegistroEstudianteService implements RegistroEstudianteUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final VerificacionOTPService otpService;

    @Value("${app.allowed-domains}")
    private String allowedDomainsConfig;

    public RegistroEstudianteService(RegistroOrchestrator registroOrchestrator,
                                     VerificacionOTPService otpService) {
        this.registroOrchestrator = registroOrchestrator;
        this.otpService = otpService;
    }

    @Override
    public Usuario registrarEstudiante(String nombreCompleto, String correo, String contrasena,
                                       String programaAcademico, Integer semestre,
                                       String tipoIdentificacion, String numeroIdentificacion) {
        validarDominioCorreo(correo);

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setProgramaAcademico(programaAcademico);
        usuario.setSemestre(semestre);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setTipoUsuario(UserType.STUDENT);

        Usuario usuarioGuardado = registroOrchestrator.registrar(usuario, contrasena);
        otpService.generarYEnviarOTP(usuarioGuardado.getId(), usuarioGuardado.getCorreo());

        return usuarioGuardado;
    }

    private void validarDominioCorreo(String correo) {
        List<String> dominiosPermitidos = Arrays.asList(allowedDomainsConfig.split(","));
        String dominio = correo.substring(correo.indexOf('@'));

        if (!dominiosPermitidos.contains(dominio)) {
            throw new BadRequestException(
                "El correo debe pertenecer a uno de los dominios permitidos: " + allowedDomainsConfig
            );
        }
    }
}
