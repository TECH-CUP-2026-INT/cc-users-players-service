package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest.swagger;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.VerificarOTPRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

/**
 * Documentación Swagger de {@code OTPController}.
 */
@Tag(name = "OTP", description = "Verificación de código de un solo uso enviado por correo")
public interface OTPControllerSwagger {

    @Operation(summary = "Verificar código OTP",
            description = "Valida el código OTP recibido por correo contra el vigente para el usuario. "
                    + "Al verificarse correctamente, marca el usuario como verificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Identidad verificada",
                    content = @Content(examples = @ExampleObject(value = """
                            { "mensaje": "Identidad verificada exitosamente", "verificado": true }
                            """))),
            @ApiResponse(responseCode = "400",
                    description = "usuarioId con formato inválido, código incorrecto, expirado, "
                            + "o se excedió el máximo de intentos",
                    content = @Content(examples = @ExampleObject(value = """
                            { "error": "Código OTP incorrecto" }
                            """)))
    })
    Map<String, Object> verificarOTP(VerificarOTPRequest request);
}
