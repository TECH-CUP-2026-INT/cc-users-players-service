package com.techcup.ccusersplayers.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;
@Data
public class SportsProfileRequest {
    @NotNull(message = "El número de dorsal es requerido")
    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
    @Max(value = 99, message = "El dorsal debe ser menor a 100")
    private Integer shirtNumber;
    @NotBlank(message = "La posición es requerida")
    @Pattern(regexp = "Goalkeeper|Defender|Midfielder|Forward", 
             message = "Posición inválida. Debe ser: Goalkeeper, Defender, Midfielder o Forward")
    private String position;
    private String photoUrl;
    private UUID teamId;
}