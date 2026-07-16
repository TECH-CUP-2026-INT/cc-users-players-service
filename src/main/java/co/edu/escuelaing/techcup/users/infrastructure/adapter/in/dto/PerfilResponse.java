package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PerfilResponse {
    private String id;
    private String nombreCompleto;
    private String correo;
    private String tipoUsuario;
    private String rol;
    private String estado;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String programaAcademico;
    private Integer semestre;
    private Boolean verificadoOTP;
    private LocalDateTime fechaRegistro;
}
