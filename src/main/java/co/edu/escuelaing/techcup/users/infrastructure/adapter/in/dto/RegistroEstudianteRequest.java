package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroEstudianteRequest {
    @Schema(description = "Nombre completo del estudiante", example = "Ada Lovelace")
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    @Schema(description = "Correo institucional; debe pertenecer a un dominio permitido",
            example = "ada.lovelace@university.edu.co")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoInstitucional;

    @Schema(description = "Contraseña de acceso, mínimo 8 caracteres", example = "Password123!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @Schema(description = "Programa académico en curso", example = "Ingeniería de Sistemas")
    @NotBlank(message = "El programa académico es obligatorio")
    private String programaAcademico;

    @Schema(description = "Semestre actual (1 a 12)", example = "5")
    @NotNull(message = "El semestre es obligatorio")
    @Min(1)
    @Max(12)
    private Integer semestre;

    @Schema(description = "Tipo de documento de identificación", example = "CC")
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @Schema(description = "Número de documento de identificación", example = "1002345678")
    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(min = 4, max = 20)
    private String numeroIdentificacion;
}
