package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarOTPUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = OTPController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class OTPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerificarOTPUseCase verificarOTPUseCase;

    @Test
    void verificaOtpExitosamente() throws Exception {
        UUID usuarioId = UUID.randomUUID();

        mockMvc.perform(post("/otp/verificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"" + usuarioId + "\",\"codigoOTP\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificado").value(true));

        verify(verificarOTPUseCase).verificarOTP(eq(usuarioId), eq("123456"));
    }

    @Test
    void rechazaUsuarioIdConFormatoInvalido() throws Exception {
        mockMvc.perform(post("/otp/verificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"no-es-un-uuid\",\"codigoOTP\":\"123456\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El campo 'usuarioId' no tiene un formato UUID válido"));

        verifyNoInteractions(verificarOTPUseCase);
    }

    @Test
    void propagaCodigoOtpIncorrectoComo400() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        doThrow(new BadRequestException("Código OTP incorrecto"))
                .when(verificarOTPUseCase).verificarOTP(any(), any());

        mockMvc.perform(post("/otp/verificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"" + usuarioId + "\",\"codigoOTP\":\"000000\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Código OTP incorrecto"));
    }
}
