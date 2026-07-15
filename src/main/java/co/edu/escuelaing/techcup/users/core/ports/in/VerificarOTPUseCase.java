package co.edu.escuelaing.techcup.users.core.ports.in;

public interface VerificarOTPUseCase {
    void verificarOTP(String usuarioId, String codigoOTP);
    void generarYEnviarOTP(String usuarioId, String email);
}
