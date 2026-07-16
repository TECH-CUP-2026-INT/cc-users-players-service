package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.email;

import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Component
public class EmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    public EmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarCorreoOTP(String destinatario, String codigoOTP) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Verifica tu identidad - TechCup");
            helper.setText(generarHtmlOTP(codigoOTP), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

    @Override
    public void enviarCorreoCredencialesTemporales(String destinatario, String contrasenaTemporal) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Tu cuenta ha sido creada - TechCup");
            helper.setText(generarHtmlCredencialesTemporales(contrasenaTemporal), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

    private String generarHtmlCredencialesTemporales(String contrasenaTemporal) {
        return """
            <!DOCTYPE html>
            <html>
            <head><style>
                body { font-family: Arial; background: #f4f4f4; padding: 20px; }
                .container { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; }
                .password-box { font-size: 24px; font-weight: bold; text-align: center; color: #333; padding: 20px; background: #f0f0f0; border-radius: 8px; letter-spacing: 2px; margin: 20px 0; }
                .footer { text-align: center; margin-top: 30px; font-size: 12px; color: #999; }
            </style></head>
            <body>
                <div class="container">
                    <h2>🏆 TechCup - Tu cuenta ha sido creada</h2>
                    <p>Un administrador creó una cuenta para ti. Esta es tu contraseña temporal:</p>
                    <div class="password-box">%s</div>
                    <p>Por seguridad, te recomendamos cambiarla después de tu primer inicio de sesión.</p>
                    <div class="footer">© %d TechCup</div>
                </div>
            </body>
            </html>
            """.formatted(contrasenaTemporal, LocalDateTime.now().getYear());
    }

    private String generarHtmlOTP(String codigoOTP) {
        return """
            <!DOCTYPE html>
            <html>
            <head><style>
                body { font-family: Arial; background: #f4f4f4; padding: 20px; }
                .container { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; }
                .otp-code { font-size: 32px; font-weight: bold; text-align: center; color: #333; padding: 20px; background: #f0f0f0; border-radius: 8px; letter-spacing: 5px; margin: 20px 0; }
                .footer { text-align: center; margin-top: 30px; font-size: 12px; color: #999; }
            </style></head>
            <body>
                <div class="container">
                    <h2>🏆 TechCup - Verifica tu identidad</h2>
                    <p>Usa el siguiente código para completar el proceso:</p>
                    <div class="otp-code">%s</div>
                    <p>Este código expirará en 15 minutos.</p>
                    <div class="footer">© %d TechCup</div>
                </div>
            </body>
            </html>
            """.formatted(codigoOTP, LocalDateTime.now().getYear());
    }
}
