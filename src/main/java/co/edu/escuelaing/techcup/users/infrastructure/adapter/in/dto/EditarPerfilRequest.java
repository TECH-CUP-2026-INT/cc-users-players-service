package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarPerfilRequest {
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    private String programaAcademico;

    @Min(1)
    @Max(12)
    private Integer semestre;
}
