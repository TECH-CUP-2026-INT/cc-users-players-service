package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEstudianteUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class RegistroEstudianteService implements RegistroEstudianteUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VerificacionOTPService otpService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("\")
    private String allowedDomainsConfig;

    public RegistroEstudianteService(UsuarioRepositoryPort usuarioRepository,
                                     VerificacionOTPService otpService,
                                     BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrarEstudiante(String nombreCompleto, String correo, String contrasena,
                                       String programaAcademico, Integer semestre,
                                       String tipoIdentificacion, String numeroIdentificacion) {
        validarDominioCorreo(correo);

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ConflictException("El correo ya está registrado");
        }

        if (usuarioRepository.existsByNumeroIdentificacion(numeroIdentificacion)) {
            throw new ConflictException("El número de identificación ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setContrasenaHash(passwordEncoder.encode(contrasena));
        usuario.setProgramaAcademico(programaAcademico);
        usuario.setSemestre(semestre);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        otpService.generarYEnviarOTP(usuarioGuardado.getUsuarioId(), usuarioGuardado.getCorreo());

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
