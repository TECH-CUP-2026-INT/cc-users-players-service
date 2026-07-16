package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroEstudianteServiceTest {

    @Mock
    private RegistroOrchestrator registroOrchestrator;
    @Mock
    private VerificacionOTPService otpService;

    private RegistroEstudianteService service;

    @BeforeEach
    void setUp() {
        service = new RegistroEstudianteService(registroOrchestrator, otpService);
        ReflectionTestUtils.setField(service, "allowedDomainsConfig", "@university.edu.co,@uniandes.edu.co");
    }

    @Test
    void registraEstudianteConCorreoDeDominioPermitidoYEnviaOTP() {
        when(registroOrchestrator.registrar(any(Usuario.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.registrarEstudiante(
                "Ada Lovelace", "ada@university.edu.co", "Password123!",
                "Ingeniería de Sistemas", 5, "CC", "111222333");

        assertThat(resultado.getTipoUsuario()).isEqualTo(UserType.STUDENT);
        verify(otpService).generarYEnviarOTP(resultado.getId(), "ada@university.edu.co");
    }

    @Test
    void rechazaCorreoDeDominioNoPermitido() {
        assertThatThrownBy(() -> service.registrarEstudiante(
                "Ada Lovelace", "ada@gmail.com", "Password123!",
                "Ingeniería de Sistemas", 5, "CC", "111222333"))
                .isInstanceOf(BadRequestException.class);

        verifyNoInteractions(registroOrchestrator, otpService);
    }
}
