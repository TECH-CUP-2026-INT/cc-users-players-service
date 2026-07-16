package co.edu.escuelaing.techcup.users.core.ports.in;

import java.util.UUID;

/**
 * Generación, envío y verificación de códigos OTP de un solo uso.
 */
public interface VerificarOTPUseCase {

    /**
     * Valida el código OTP ingresado contra el vigente para el usuario.
     *
     * @throws co.edu.escuelaing.techcup.users.core.exception.BadRequestException
     *         si el código es inválido, expiró o se excedió el máximo de intentos
     */
    void verificarOTP(UUID usuarioId, String codigoOTP);

    /** Genera un nuevo código OTP, lo persiste y lo envía por correo. */
    void generarYEnviarOTP(UUID usuarioId, String email);
}
