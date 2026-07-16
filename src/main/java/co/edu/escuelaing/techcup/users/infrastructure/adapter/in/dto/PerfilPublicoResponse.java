package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilPublicoResponse {
    private String id;
    private String nombreCompleto;
    private String tipoUsuario;
    private String rol;
    private String programaAcademico;
}
