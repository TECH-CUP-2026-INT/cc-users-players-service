package co.edu.escuelaing.techcup.users.core.ports.out;

import java.util.Optional;

public interface IdentityTokenValidationPort {

    Optional<TokenInfo> validar(String bearerToken);

    record TokenInfo(String userId, String email, String role) {
    }
}
