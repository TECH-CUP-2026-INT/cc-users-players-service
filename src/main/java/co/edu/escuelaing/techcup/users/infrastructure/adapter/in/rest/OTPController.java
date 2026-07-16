package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.ports.in.VerificarOTPUseCase;
import co.edu.escuelaing.techcup.users.core.util.UuidParser;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.VerificarOTPRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest.swagger.OTPControllerSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OTPController implements OTPControllerSwagger {

    private final VerificarOTPUseCase verificarOTPUseCase;

    @Override
    @PostMapping("/verificar")
    public Map<String, Object> verificarOTP(@Valid @RequestBody VerificarOTPRequest request) {
        verificarOTPUseCase.verificarOTP(
                UuidParser.parse(request.getUsuarioId(), "usuarioId"), request.getCodigoOTP());
        return Map.of(
            "mensaje", "Identidad verificada exitosamente",
            "verificado", true
        );
    }
}
