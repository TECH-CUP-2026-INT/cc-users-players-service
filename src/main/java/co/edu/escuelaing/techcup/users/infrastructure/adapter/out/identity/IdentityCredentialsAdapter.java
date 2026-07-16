package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.CreateCredentialsRequestDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.UpdateRoleRequestDTO;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class IdentityCredentialsAdapter implements IdentityCredentialsPort {

    private final IdentityFeignClient identityFeignClient;

    public IdentityCredentialsAdapter(IdentityFeignClient identityFeignClient) {
        this.identityFeignClient = identityFeignClient;
    }

    @Override
    public void crearCredenciales(String userId, String email, String password,
                                   String fullName, UserType tipoUsuario, UserRole rol) {
        try {
            identityFeignClient.crearCredenciales(
                    new CreateCredentialsRequestDTO(userId, email, password, fullName, tipoUsuario, rol));
        } catch (FeignException e) {
            throw new IdentityIntegrationException(
                    "No se pudieron crear las credenciales en Identity Service", e);
        }
    }

    @Override
    public void actualizarRol(String userId, UserRole rol) {
        try {
            identityFeignClient.actualizarRol(userId, new UpdateRoleRequestDTO(rol));
        } catch (FeignException e) {
            throw new IdentityIntegrationException(
                    "No se pudo actualizar el rol en Identity Service", e);
        }
    }
}
