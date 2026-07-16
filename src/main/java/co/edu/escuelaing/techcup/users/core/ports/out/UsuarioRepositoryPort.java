package co.edu.escuelaing.techcup.users.core.ports.out;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(UUID id);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByCorreo(String correo);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    void deleteById(UUID id);
}
