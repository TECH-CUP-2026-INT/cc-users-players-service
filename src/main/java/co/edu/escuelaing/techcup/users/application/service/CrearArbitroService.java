package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.in.CrearArbitroUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import org.springframework.stereotype.Service;

@Service
public class CrearArbitroService implements CrearArbitroUseCase {

    private final RegistroOrchestrator registroOrchestrator;
    private final EmailSenderPort emailSender;

    public CrearArbitroService(RegistroOrchestrator registroOrchestrator, EmailSenderPort emailSender) {
        this.registroOrchestrator = registroOrchestrator;
        this.emailSender = emailSender;
    }

    @Override
    public Usuario crearArbitro(String nombreCompleto, String correo,
                                 String tipoIdentificacion, String numeroIdentificacion) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setTipoIdentificacion(tipoIdentificacion);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setTipoUsuario(UserType.REFEREE);
        usuario.setRol(UserRole.REFEREE);

        String contrasenaTemporal = TemporaryPasswordGenerator.generar();
        Usuario usuarioGuardado = registroOrchestrator.registrar(usuario, contrasenaTemporal);
        emailSender.enviarCorreoCredencialesTemporales(usuarioGuardado.getCorreo(), contrasenaTemporal);

        return usuarioGuardado;
    }
}
