package co.edu.escuelaing.techcup.users.core.domain;

import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.domain.enums.PosicionJuego;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Perfil de un usuario de la plataforma (estudiante, invitado, egresado,
 * árbitro, admin u organizador). Es la fuente de verdad de identidad para
 * todos los microservicios; Identity Service solo guarda credenciales
 * referenciando este {@code id}.
 */
@Getter
@Setter
public class Usuario {
    private UUID id;
    private String nombreCompleto;
    private String correo;
    private String contrasenaHash;
    private String programaAcademico;
    private Integer semestre;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private UserType tipoUsuario;
    private AccountStatus estado;
    private UserRole rol;
    private Boolean verificadoOTP;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
    private String motivoDeshabilitacion;

    // Perfil deportivo (TC-17)
    private PosicionJuego posicionJuego;
    private Integer numeroCamiseta;
    private byte[] fotoPerfil;
    private String fotoPerfilContentType;

    public Usuario() {
        this.id = UUID.randomUUID();
        this.estado = AccountStatus.ACTIVE;
        this.rol = UserRole.PLAYER;
        this.verificadoOTP = false;
        this.fechaRegistro = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

}
