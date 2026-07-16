package co.edu.escuelaing.techcup.users.core.ports.in;

import java.util.UUID;

public interface VerificarOTPUseCase {
    void verificarOTP(UUID usuarioId, String codigoOTP);
    void generarYEnviarOTP(UUID usuarioId, String email);
}
