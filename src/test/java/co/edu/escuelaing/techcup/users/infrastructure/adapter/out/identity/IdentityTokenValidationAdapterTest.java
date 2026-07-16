package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.TokenValidationResponseDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentityTokenValidationAdapterTest {

    @Mock
    private IdentityFeignClient identityFeignClient;

    private IdentityTokenValidationAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new IdentityTokenValidationAdapter(identityFeignClient);
    }

    @Test
    void retornaTokenInfoCuandoElTokenEsValidoYEsAdmin() {
        when(identityFeignClient.validar("Bearer abc123"))
                .thenReturn(new TokenValidationResponseDTO(true, "u1", "admin@techcup.com", "ADMIN"));

        Optional<IdentityTokenValidationPort.TokenInfo> resultado = adapter.validar("Bearer abc123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().role()).isEqualTo("ADMIN");
    }

    @Test
    void retornaVacioCuandoIdentityRespondeInvalido() {
        when(identityFeignClient.validar("Bearer expirado"))
                .thenReturn(new TokenValidationResponseDTO(false, null, null, null));

        assertThat(adapter.validar("Bearer expirado")).isEmpty();
    }

    @Test
    void retornaVacioCuandoIdentityRespondeError() {
        when(identityFeignClient.validar("Bearer invalido")).thenThrow(mock(FeignException.class));

        assertThat(adapter.validar("Bearer invalido")).isEmpty();
    }
}
