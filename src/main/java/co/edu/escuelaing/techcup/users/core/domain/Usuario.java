package co.edu.escuelaing.techcup.users.core.domain;

import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nombreCompleto;
    private String correo;
    private String contrasenaHash;
    private String programaAcademico;
    private Integer semestre;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private UserType tipoUsuario;
    private AccountStatus estado;
    private UserRole rol;
    private Boolean verificadoOTP;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Usuario() {
        this.id = UUID.randomUUID();
        this.estado = AccountStatus.ACTIVE;
        this.rol = UserRole.PLAYER;
        this.verificadoOTP = false;
        this.fechaRegistro = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
    public String getProgramaAcademico() { return programaAcademico; }
    public void setProgramaAcademico(String programaAcademico) { this.programaAcademico = programaAcademico; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }
    public UserType getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(UserType tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public AccountStatus getEstado() { return estado; }
    public void setEstado(AccountStatus estado) { this.estado = estado; }
    public UserRole getRol() { return rol; }
    public void setRol(UserRole rol) { this.rol = rol; }
    public Boolean getVerificadoOTP() { return verificadoOTP; }
    public void setVerificadoOTP(Boolean verificadoOTP) { this.verificadoOTP = verificadoOTP; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
