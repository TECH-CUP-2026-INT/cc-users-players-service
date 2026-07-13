package com.techcup.ccusersplayers.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CaptainToggleRequest {
    @NotNull(message = "El estado es requerido")
    private Boolean activate;
}