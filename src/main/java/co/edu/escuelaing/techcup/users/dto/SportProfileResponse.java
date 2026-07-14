package co.edu.escuelaing.techcup.users.dto;

import co.edu.escuelaing.techcup.users.entity.SportProfile.Position;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Response DTO for sports profile data.
 */
@Data
@Builder
public class SportProfileResponse {

    private String   id;
    private String   userId;
    private Position position;
    private Integer  jerseyNumber;
    private String   photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}