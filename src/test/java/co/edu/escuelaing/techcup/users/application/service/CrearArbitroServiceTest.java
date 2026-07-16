package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearArbitroServiceTest {

    @Mock
    private RegistroOrchestrator registroOrchestrator;
    @Mock
    private EmailSenderPort emailSender;

    private CrearArbitroService service;

    @BeforeEach
    void setUp() {
        service = new CrearArbitroService(registroOrchestrator, emailSender);
    }

    @Test
    void creaArbitroConCredencialesTemporalesYLasEnviaPorCorreo() {
        when(registroOrchestrator.registrar(any(Usuario.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.crearArbitro("Juan Referí", "juan@techcup.com", TipoIdentificacion.CC, "555444");

        assertThat(resultado.getTipoUsuario()).isEqualTo(UserType.REFEREE);
        assertThat(resultado.getRol()).isEqualTo(UserRole.REFEREE);
        verify(emailSender).enviarCorreoCredencialesTemporales(eq("juan@techcup.com"), anyString());
    }
}
