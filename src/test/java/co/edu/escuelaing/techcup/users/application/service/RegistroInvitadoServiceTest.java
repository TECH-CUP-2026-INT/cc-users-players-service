package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroInvitadoServiceTest {

    @Mock
    private RegistroOrchestrator registroOrchestrator;
    @Mock
    private VerificacionOTPService otpService;

    private RegistroInvitadoService service;

    @BeforeEach
    void setUp() {
        service = new RegistroInvitadoService(registroOrchestrator, otpService);
    }

    @Test
    void registraInvitadoYEnviaOTP() {
        when(registroOrchestrator.registrar(any(Usuario.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.registrarInvitado(
                "Invitado Uno", "invitado@externo.com", "Password123!", "CC", "333222");

        assertThat(resultado.getTipoUsuario()).isEqualTo(UserType.GUEST);
        verify(otpService).generarYEnviarOTP(resultado.getId(), "invitado@externo.com");
    }
}
