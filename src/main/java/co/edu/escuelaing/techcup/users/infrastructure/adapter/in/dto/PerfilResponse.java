package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PerfilResponse {
    @Schema(description = "Id del usuario (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String id;

    @Schema(description = "Nombre completo", example = "Ada Lovelace")
    private String nombreCompleto;

    @Schema(description = "Correo registrado", example = "ada.lovelace@university.edu.co")
    private String correo;

    @Schema(description = "Tipo de usuario", example = "STUDENT")
    private String tipoUsuario;

    @Schema(description = "Rol actual", example = "PLAYER")
    private String rol;

    @Schema(description = "Estado de la cuenta", example = "ACTIVE")
    private String estado;

    @Schema(description = "Tipo de documento de identificación", example = "CC")
    private String tipoIdentificacion;

    @Schema(description = "Número de documento de identificación", example = "1002345678")
    private String numeroIdentificacion;

    @Schema(description = "Programa académico", example = "Ingeniería de Sistemas")
    private String programaAcademico;

    @Schema(description = "Semestre actual", example = "5")
    private Integer semestre;

    @Schema(description = "Si el usuario ya verificó su identidad con OTP", example = "true")
    private Boolean verificadoOTP;

    @Schema(description = "Fecha de registro", example = "2026-02-10T14:30:00")
    private LocalDateTime fechaRegistro;
}
