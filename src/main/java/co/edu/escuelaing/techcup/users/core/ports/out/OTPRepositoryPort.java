package co.edu.escuelaing.techcup.users.core.ports.out;

import co.edu.escuelaing.techcup.users.core.domain.OTP;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistencia de {@link co.edu.escuelaing.techcup.users.core.domain.OTP}.
 */
public interface OTPRepositoryPort {
    OTP save(OTP otp);
    Optional<OTP> findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(UUID usuarioId, LocalDateTime now);
}
