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
