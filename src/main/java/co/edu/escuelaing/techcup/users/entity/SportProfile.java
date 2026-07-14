package co.edu.escuelaing.techcup.users.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

/**
 * Represents the sports profile of a registered player.
 * Stored in the {@code sport_profiles} MongoDB collection.
 *
 * Each player has at most one sports profile linked by their userId
 * from the identity-service.
 *
 * Covered requirements:
 *   TC-14 — Consult Sports Profile
 *   TC-17 — Update Sports Profile
 */
@Document(collection = "sport_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportProfile {

    @Id
    private String id;

    /**
     * References the user in identity-service.
     * Indexed and unique — one sports profile per player.
     */
    @Indexed(unique = true)
    private String userId;

    /**
     * Player's preferred field position.
     */
    private Position position;

    /**
     * Player's jersey number.
     * Uniqueness within a team is enforced at the teams-service level.
     */
    private Integer jerseyNumber;

    /**
     * URL or path to the player's profile photo.
     * Null when no photo has been uploaded.
     */
    private String photoUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum Position {
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD
    }
}