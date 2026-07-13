package com.techcup.ccusersplayers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportsProfileResponse {
    private UUID id;
    private UUID userId;
    private Integer shirtNumber;
    private String position;
    private String photoUrl;
    private UUID teamId;
    private Boolean isCaptain;
    private String message;
}