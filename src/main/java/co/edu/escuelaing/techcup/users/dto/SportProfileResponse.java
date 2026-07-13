package co.edu.escuelaing.techcup.users.dto;

import co.edu.escuelaing.techcup.users.entity.SportProfile.Position;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for sports profile data.
 * Returned by TC-14 (consult) and TC-17 (update).
 */
@Data
@Builder
public class SportProfileResponse {

    private UUID     id;
    private UUID     userId;
    private Position position;
    private Integer  jerseyNumber;

    /**
     * URL or path to the player's photo.
     * Null when no photo has been uploaded.
     */
    private String   photoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}