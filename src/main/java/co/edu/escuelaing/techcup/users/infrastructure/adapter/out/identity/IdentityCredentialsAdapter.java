package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.ports.out.IdentityCredentialsPort;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.CreateCredentialsRequestDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.UpdateRoleRequestDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.UpdateStatusRequestDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador saliente hacia los endpoints internos de Identity Service para
 * crear credenciales y sincronizar rol/estado de un usuario.
 */
@Component
@RequiredArgsConstructor
public class IdentityCredentialsAdapter implements IdentityCredentialsPort {

    private final IdentityFeignClient identityFeignClient;

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

    @Override
    public void actualizarEstado(String userId, AccountStatus estado) {
        try {
            identityFeignClient.actualizarEstado(userId, new UpdateStatusRequestDTO(estado));
        } catch (FeignException e) {
            throw new IdentityIntegrationException(
                    "No se pudo actualizar el estado en Identity Service", e);
        }
    }
}
