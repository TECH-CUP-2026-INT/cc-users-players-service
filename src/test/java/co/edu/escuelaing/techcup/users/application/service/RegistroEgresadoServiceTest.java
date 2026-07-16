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
class RegistroEgresadoServiceTest {

    @Mock
    private RegistroOrchestrator registroOrchestrator;
    @Mock
    private VerificacionOTPService otpService;

    private RegistroEgresadoService service;

    @BeforeEach
    void setUp() {
        service = new RegistroEgresadoService(registroOrchestrator, otpService);
    }

    @Test
    void registraEgresadoConProgramaOpcionalYEnviaOTP() {
        when(registroOrchestrator.registrar(any(Usuario.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.registrarEgresado(
                "Egresado Uno", "egresado@gmail.com", "Password123!", "CC", "444555", "Ingeniería de Sistemas");

        assertThat(resultado.getTipoUsuario()).isEqualTo(UserType.GRADUATE);
        assertThat(resultado.getProgramaAcademico()).isEqualTo("Ingeniería de Sistemas");
        verify(otpService).generarYEnviarOTP(resultado.getId(), "egresado@gmail.com");
    }
}
