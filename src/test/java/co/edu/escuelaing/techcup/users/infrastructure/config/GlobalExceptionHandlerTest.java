package co.edu.escuelaing.techcup.users.infrastructure.config;

import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.exception.ConflictException;
import co.edu.escuelaing.techcup.users.core.exception.IdentityIntegrationException;
import co.edu.escuelaing.techcup.users.core.exception.TeamsIntegrationException;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejaBadRequestException() {
        Map<String, Object> body = handler.handleBadRequest(new BadRequestException("dato inválido"));
        assertThat(body).containsEntry("error", "dato inválido");
    }

    @Test
    void manejaConflictException() {
        Map<String, Object> body = handler.handleConflict(new ConflictException("ya existe"));
        assertThat(body).containsEntry("error", "ya existe");
    }

    @Test
    void manejaIdentityIntegrationException() {
        Map<String, Object> body = handler.handleIdentityIntegration(
                new IdentityIntegrationException("identity caída", new RuntimeException()));
        assertThat(body).containsEntry("error", "identity caída");
    }

    @Test
    void manejaTeamsIntegrationException() {
        Map<String, Object> body = handler.handleTeamsIntegration(
                new TeamsIntegrationException("teams caída", new RuntimeException()));
        assertThat(body).containsEntry("error", "teams caída");
    }

    @Test
    void manejaErroresDeValidacion() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "correo", "El correo es obligatorio"));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        Map<String, Object> body = handler.handleValidationExceptions(ex);

        assertThat(body.get("error")).isEqualTo("Datos inválidos");
        @SuppressWarnings("unchecked")
        Map<String, String> detalles = (Map<String, String>) body.get("detalles");
        assertThat(detalles).containsEntry("correo", "El correo es obligatorio");
    }
}
