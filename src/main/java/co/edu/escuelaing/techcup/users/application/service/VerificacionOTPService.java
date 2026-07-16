package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.OTP;
import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarOTPUseCase;
import co.edu.escuelaing.techcup.users.core.ports.out.OTPRepositoryPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Genera, envía y verifica códigos OTP de un solo uso, aplicando límites de
 * expiración e intentos fallidos configurables.
 */
@Service
@RequiredArgsConstructor
public class VerificacionOTPService implements VerificarOTPUseCase {

    private final OTPRepositoryPort otpRepository;
    private final UsuarioRepositoryPort usuarioRepository;
    private final EmailSenderPort emailSender;

    @Value("${app.otp.expiration-minutes}")
    private int expirationMinutes;

    @Value("${app.otp.max-attempts}")
    private int maxAttempts;

    @Override
    public void generarYEnviarOTP(UUID usuarioId, String email) {
        String codigo = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusMinutes(expirationMinutes);

        OTP otp = new OTP();
        otp.setUsuarioId(usuarioId);
        otp.setCodigoOTP(codigo);
        otp.setFechaExpiracion(fechaExpiracion);

        otpRepository.save(otp);
        emailSender.enviarCorreoOTP(email, codigo);
    }

    @Override
    public void verificarOTP(UUID usuarioId, String codigoIngresado) {
        OTP otp = otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(
            usuarioId, LocalDateTime.now()
        ).orElseThrow(() -> new BadRequestException("Código OTP inválido o expirado"));

        if (otp.getIntentosFallidos() >= maxAttempts) {
            throw new BadRequestException("Has excedido el número máximo de intentos");
        }

        if (!otp.getCodigoOTP().equals(codigoIngresado)) {
            otp.setIntentosFallidos(otp.getIntentosFallidos() + 1);
            otpRepository.save(otp);
            throw new BadRequestException("Código OTP incorrecto");
        }

        otp.setUsado(true);
        otpRepository.save(otp);

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        usuario.setVerificadoOTP(true);
        usuarioRepository.save(usuario);
    }
}
