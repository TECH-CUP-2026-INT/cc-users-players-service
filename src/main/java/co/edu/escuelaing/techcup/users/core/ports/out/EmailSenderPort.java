package co.edu.escuelaing.techcup.users.core.ports.out;

public interface EmailSenderPort {
    void enviarCorreoOTP(String destinatario, String codigoOTP);
    void enviarCorreoCredencialesTemporales(String destinatario, String contrasenaTemporal);
}
