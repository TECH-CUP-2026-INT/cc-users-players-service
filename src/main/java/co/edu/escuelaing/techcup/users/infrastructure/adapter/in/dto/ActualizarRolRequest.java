package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarRolRequest {
    @NotNull(message = "El rol es obligatorio")
    private UserRole rol;
}
