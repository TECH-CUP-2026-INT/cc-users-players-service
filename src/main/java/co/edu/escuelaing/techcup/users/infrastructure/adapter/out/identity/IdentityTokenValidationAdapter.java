package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.TokenValidationResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador saliente que delega la validación de JWT en Identity Service
 * (autenticación remota vía Feign, sin secreto local).
 */
@Component
@RequiredArgsConstructor
public class IdentityTokenValidationAdapter implements IdentityTokenValidationPort {

    private final IdentityFeignClient identityFeignClient;

    @Override
    public Optional<TokenInfo> validar(String bearerToken) {
        try {
            TokenValidationResponseDTO response = identityFeignClient.validar(bearerToken);

            if (response == null || !response.valid()) {
                return Optional.empty();
            }
            return Optional.of(new TokenInfo(response.userId(), response.email(), response.role()));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }
}
