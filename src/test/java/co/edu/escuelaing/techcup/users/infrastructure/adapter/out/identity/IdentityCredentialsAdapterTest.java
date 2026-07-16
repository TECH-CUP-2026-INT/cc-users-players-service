package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.CreateCredentialsRequestDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.UpdateRoleRequestDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IdentityCredentialsAdapterTest {

    @Mock
    private IdentityFeignClient identityFeignClient;

    private IdentityCredentialsAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new IdentityCredentialsAdapter(identityFeignClient);
    }

    @Test
    void envíaElPayloadCorrectoAIdentity() {
        adapter.crearCredenciales("u1", "ada@techcup.com", "Password123!", "Ada Lovelace",
                UserType.STUDENT, UserRole.PLAYER);

        verify(identityFeignClient).crearCredenciales(
                new CreateCredentialsRequestDTO("u1", "ada@techcup.com", "Password123!",
                        "Ada Lovelace", UserType.STUDENT, UserRole.PLAYER));
    }

    @Test
    void lanzaIdentityIntegrationExceptionSiIdentityResponde409() {
        doThrow(mock(FeignException.class)).when(identityFeignClient).crearCredenciales(any());

        assertThatThrownBy(() -> adapter.crearCredenciales("u1", "ada@techcup.com", "Password123!",
                "Ada Lovelace", UserType.STUDENT, UserRole.PLAYER))
                .isInstanceOf(IdentityIntegrationException.class);
    }

    @Test
    void envíaElPutDeActualizarRolAIdentity() {
        adapter.actualizarRol("u1", UserRole.CAPTAIN);

        verify(identityFeignClient).actualizarRol(eq("u1"), eq(new UpdateRoleRequestDTO(UserRole.CAPTAIN)));
    }

    @Test
    void lanzaIdentityIntegrationExceptionSiFallaLaActualizacionDeRol() {
        doThrow(mock(FeignException.class)).when(identityFeignClient).actualizarRol(any(), any());

        assertThatThrownBy(() -> adapter.actualizarRol("u1", UserRole.CAPTAIN))
                .isInstanceOf(IdentityIntegrationException.class);
    }
}
