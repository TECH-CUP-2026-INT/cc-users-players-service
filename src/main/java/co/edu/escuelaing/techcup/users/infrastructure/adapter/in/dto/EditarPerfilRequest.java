package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarPerfilRequest {
    @Schema(description = "Nuevo nombre completo (opcional)", example = "Ada Lovelace Actualizada")
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    @Schema(description = "Nuevo programa académico (opcional)", example = "Ingeniería de Sistemas")
    private String programaAcademico;

    @Schema(description = "Nuevo semestre, 1 a 12 (opcional)", example = "6")
    @Min(1)
    @Max(12)
    private Integer semestre;
}
