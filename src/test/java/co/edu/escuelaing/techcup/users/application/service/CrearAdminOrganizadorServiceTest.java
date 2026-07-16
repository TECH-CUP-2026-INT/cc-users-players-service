package co.edu.escuelaing.techcup.users.application.service;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.ports.out.EmailSenderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearAdminOrganizadorServiceTest {

    @Mock
    private RegistroOrchestrator registroOrchestrator;
    @Mock
    private EmailSenderPort emailSender;

    private CrearAdminOrganizadorService service;

    @BeforeEach
    void setUp() {
        service = new CrearAdminOrganizadorService(registroOrchestrator, emailSender);
    }

    @Test
    void creaAdminConCredencialesTemporalesYLasEnviaPorCorreo() {
        when(registroOrchestrator.registrar(any(Usuario.class), anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = service.crearAdminOrganizador(
                "Grace Hopper", "grace@techcup.com", TipoIdentificacion.CC, "999888", UserRole.ADMIN);

        assertThat(resultado.getTipoUsuario()).isEqualTo(UserType.ADMIN);
        assertThat(resultado.getRol()).isEqualTo(UserRole.ADMIN);
        verify(emailSender).enviarCorreoCredencialesTemporales(eq("grace@techcup.com"), anyString());
    }

    @Test
    void rechazaRolDistintoDeAdminUOrganizer() {
        assertThatThrownBy(() -> service.crearAdminOrganizador(
                "Jugador X", "jugadorx@techcup.com", TipoIdentificacion.CC, "111222", UserRole.PLAYER))
                .isInstanceOf(BadRequestException.class);

        verifyNoInteractions(registroOrchestrator, emailSender);
    }
}
