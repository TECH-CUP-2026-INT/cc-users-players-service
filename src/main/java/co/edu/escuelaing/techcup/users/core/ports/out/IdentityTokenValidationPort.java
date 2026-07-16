package co.edu.escuelaing.techcup.users.core.ports.out;

import java.util.Optional;

/**
 * Validación remota de JWT contra Identity Service. Este servicio no posee
 * un secreto local: toda autenticación se delega vía esta integración.
 */
public interface IdentityTokenValidationPort {

    /**
     * @return {@link Optional#empty()} si el token es inválido, expiró, fue
     *         revocado, o Identity Service no está disponible
     */
    Optional<TokenInfo> validar(String bearerToken);

    record TokenInfo(String userId, String email, String role) {
    }
}
