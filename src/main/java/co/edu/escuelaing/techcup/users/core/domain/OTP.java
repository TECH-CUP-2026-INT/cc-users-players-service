package co.edu.escuelaing.techcup.users.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class OTP {
    private UUID id;
    private UUID usuarioId;
    private String codigoOTP;
    private LocalDateTime fechaExpiracion;
    private Boolean usado;
    private Integer intentosFallidos;
    private LocalDateTime fechaCreacion;

    public OTP() {
        this.id = UUID.randomUUID();
        this.usado = false;
        this.intentosFallidos = 0;
        this.fechaCreacion = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public String getCodigoOTP() { return codigoOTP; }
    public void setCodigoOTP(String codigoOTP) { this.codigoOTP = codigoOTP; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public Boolean getUsado() { return usado; }
    public void setUsado(Boolean usado) { this.usado = usado; }
    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
