package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.OTP;
import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.NotFoundException;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import co.edu.escuelaing.techcup.users.core.ports.out.OTPRepositoryPort;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificacionOTPServiceTest {

    @Mock
    private OTPRepositoryPort otpRepository;
    @Mock
    private UsuarioRepositoryPort usuarioRepository;
    @Mock
    private EmailSenderPort emailSender;

    private VerificacionOTPService service;

    private final UUID usuarioId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new VerificacionOTPService(otpRepository, usuarioRepository, emailSender);
        ReflectionTestUtils.setField(service, "expirationMinutes", 15);
        ReflectionTestUtils.setField(service, "maxAttempts", 3);
    }

    @Test
    void generaYGuardaOtpYEnviaCorreo() {
        when(otpRepository.save(any(OTP.class))).thenAnswer(inv -> inv.getArgument(0));

        service.generarYEnviarOTP(usuarioId, "ada@university.edu.co");

        ArgumentCaptor<OTP> captor = ArgumentCaptor.forClass(OTP.class);
        verify(otpRepository).save(captor.capture());
        assertThat(captor.getValue().getUsuarioId()).isEqualTo(usuarioId);
        assertThat(captor.getValue().getCodigoOTP()).hasSize(6);

        verify(emailSender).enviarCorreoOTP(eq("ada@university.edu.co"), anyString());
    }

    @Test
    void rechazaSiNoHayOtpVigente() {
        when(otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(eq(usuarioId), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.verificarOTP(usuarioId, "123456"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void rechazaSiSeExcedioElMaximoDeIntentos() {
        OTP otp = otpDePrueba("123456");
        otp.setIntentosFallidos(3);
        when(otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(eq(usuarioId), any()))
                .thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> service.verificarOTP(usuarioId, "123456"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void rechazaCodigoIncorrectoEIncrementaIntentos() {
        OTP otp = otpDePrueba("123456");
        when(otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(eq(usuarioId), any()))
                .thenReturn(Optional.of(otp));

        assertThatThrownBy(() -> service.verificarOTP(usuarioId, "999999"))
                .isInstanceOf(BadRequestException.class);

        assertThat(otp.getIntentosFallidos()).isEqualTo(1);
        verify(otpRepository).save(otp);
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void lanzaNotFoundSiElUsuarioNoExiste() {
        OTP otp = otpDePrueba("123456");
        when(otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(eq(usuarioId), any()))
                .thenReturn(Optional.of(otp));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.verificarOTP(usuarioId, "123456"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void verificaCorrectamenteYMarcaUsuarioComoVerificado() {
        OTP otp = otpDePrueba("123456");
        Usuario usuario = new Usuario();
        when(otpRepository.findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(eq(usuarioId), any()))
                .thenReturn(Optional.of(otp));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        service.verificarOTP(usuarioId, "123456");

        assertThat(otp.getUsado()).isTrue();
        assertThat(usuario.getVerificadoOTP()).isTrue();
        verify(otpRepository).save(otp);
        verify(usuarioRepository).save(usuario);
    }

    private OTP otpDePrueba(String codigo) {
        OTP otp = new OTP();
        otp.setUsuarioId(usuarioId);
        otp.setCodigoOTP(codigo);
        otp.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        return otp;
    }
}
