package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroResponse {
    private String usuarioId;
    private String estadoCuenta;
    private String rolAsignado;
    private String mensaje;
}
