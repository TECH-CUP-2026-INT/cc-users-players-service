package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroEstudianteRequest {
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoInstitucional;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @NotBlank(message = "El programa académico es obligatorio")
    private String programaAcademico;

    @NotNull(message = "El semestre es obligatorio")
    @Min(1)
    @Max(12)
    private Integer semestre;

    @NotBlank(message = "El tipo de identificación es obligatorio")
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(min = 4, max = 20)
    private String numeroIdentificacion;
}
