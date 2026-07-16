package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import co.edu.escuelaing.techcup.users.core.ports.in.VerificarOTPUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.VerificarOTPRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/otp")
public class OTPController {

    private final VerificarOTPUseCase verificarOTPUseCase;

    public OTPController(VerificarOTPUseCase verificarOTPUseCase) {
        this.verificarOTPUseCase = verificarOTPUseCase;
    }

    @PostMapping("/verificar")
    public Map<String, Object> verificarOTP(@Valid @RequestBody VerificarOTPRequest request) {
        verificarOTPUseCase.verificarOTP(parseUsuarioId(request.getUsuarioId()), request.getCodigoOTP());
        return Map.of(
            "mensaje", "Identidad verificada exitosamente",
            "verificado", true
        );
    }

    private UUID parseUsuarioId(String usuarioId) {
        try {
            return UUID.fromString(usuarioId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El usuarioId no tiene un formato UUID válido");
        }
    }
}
