package co.edu.escuelaing.techcup.users.core.ports.out;

/**
 * Envío de correos transaccionales a usuarios de la plataforma.
 */
public interface EmailSenderPort {

    /**
     * @throws co.edu.escuelaing.techcup.users.core.exception.EmailSenderException si falla el envío
     */
    void enviarCorreoOTP(String destinatario, String codigoOTP);

    /**
     * @throws co.edu.escuelaing.techcup.users.core.exception.EmailSenderException si falla el envío
     */
    void enviarCorreoCredencialesTemporales(String destinatario, String contrasenaTemporal);
}
