package co.edu.escuelaing.techcup.users.core.exception;

/**
 * Se lanza cuando falla el envío de un correo electrónico (OTP o credenciales
 * temporales) a través del proveedor SMTP configurado.
 */
public class EmailSenderException extends RuntimeException {
    public EmailSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
