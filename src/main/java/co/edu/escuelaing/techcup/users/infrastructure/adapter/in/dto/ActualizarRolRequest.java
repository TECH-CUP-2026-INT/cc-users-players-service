package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarRolRequest {
    @Schema(description = "Nuevo rol a asignar al usuario", example = "CAPTAIN")
    @NotNull(message = "El rol es obligatorio")
    private UserRole rol;
}
