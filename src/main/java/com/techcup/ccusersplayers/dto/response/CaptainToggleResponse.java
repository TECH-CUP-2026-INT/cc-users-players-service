package com.techcup.ccusersplayers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptainToggleResponse {
    private UUID userId;
    private Boolean isCaptain;
    private String message;
}