package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.ports.in.VerificarOTPUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.VerificarOTPRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OTPController {

    private final VerificarOTPUseCase verificarOTPUseCase;

    public OTPController(VerificarOTPUseCase verificarOTPUseCase) {
        this.verificarOTPUseCase = verificarOTPUseCase;
    }

    @PostMapping("/verificar")
    public Map<String, Object> verificarOTP(@Valid @RequestBody VerificarOTPRequest request) {
        verificarOTPUseCase.verificarOTP(request.getUsuarioId(), request.getCodigoOTP());
        return Map.of(
            "mensaje", "Identidad verificada exitosamente",
            "verificado", true
        );
    }
}
