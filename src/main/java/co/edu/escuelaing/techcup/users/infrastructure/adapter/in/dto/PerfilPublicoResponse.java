package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilPublicoResponse {
    @Schema(description = "Id del usuario (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String id;

    @Schema(description = "Nombre completo", example = "Ada Lovelace")
    private String nombreCompleto;

    @Schema(description = "Tipo de usuario", example = "STUDENT")
    private String tipoUsuario;

    @Schema(description = "Rol actual", example = "PLAYER")
    private String rol;

    @Schema(description = "Programa académico", example = "Ingeniería de Sistemas")
    private String programaAcademico;
}
