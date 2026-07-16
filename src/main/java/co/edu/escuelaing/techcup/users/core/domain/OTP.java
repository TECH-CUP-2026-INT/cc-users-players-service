package co.edu.escuelaing.techcup.users.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Código de verificación de un solo uso (OTP) asociado a un usuario, usado
 * para confirmar identidad tras el registro o el login.
 */
@Getter
@Setter
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

}
