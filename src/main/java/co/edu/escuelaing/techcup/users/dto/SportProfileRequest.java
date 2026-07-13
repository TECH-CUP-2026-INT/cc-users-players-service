package co.edu.escuelaing.techcup.users.dto;

import co.edu.escuelaing.techcup.users.entity.SportProfile.Position;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request DTO for creating or updating a sports profile.
 * Used in TC-13 (create) and TC-17 (update).
 */
@Data
public class SportProfileRequest {

    @NotNull(message = "Position is required")
    private Position position;

    @NotNull(message = "Jersey number is required")
    @Positive(message = "Jersey number must be a positive number")
    private Integer jerseyNumber;

    /**
     * Optional URL or path to the player's profile photo.
     * If null, the frontend displays the jersey number as avatar.
     */
    private String photoUrl;
}