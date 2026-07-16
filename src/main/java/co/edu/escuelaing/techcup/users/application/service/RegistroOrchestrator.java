package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Orquesta el registro común a todos los tipos de usuario: valida
 * duplicados, hashea la contraseña, persiste el perfil local y crea las
 * credenciales en Identity Service, compensando (borrando el perfil local)
 * si esa integración falla.
 */
@Component
@RequiredArgsConstructor
class RegistroOrchestrator {

    private final UsuarioRepositoryPort usuarioRepository;
    private final IdentityCredentialsPort identityCredentialsPort;
    private final BCryptPasswordEncoder passwordEncoder;

    Usuario registrar(Usuario usuario, String contrasenaPlano) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ConflictException("El correo ya está registrado");
        }
        if (usuarioRepository.existsByNumeroIdentificacion(usuario.getNumeroIdentificacion())) {
            throw new ConflictException("El número de identificación ya está registrado");
        }

        usuario.setContrasenaHash(passwordEncoder.encode(contrasenaPlano));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        try {
            identityCredentialsPort.crearCredenciales(
                    usuarioGuardado.getId().toString(),
                    usuarioGuardado.getCorreo(),
                    contrasenaPlano,
                    usuarioGuardado.getNombreCompleto(),
                    usuarioGuardado.getTipoUsuario(),
                    usuarioGuardado.getRol());
        } catch (IdentityIntegrationException e) {
            usuarioRepository.deleteById(usuarioGuardado.getId());
            throw e;
        }

        return usuarioGuardado;
    }
}
