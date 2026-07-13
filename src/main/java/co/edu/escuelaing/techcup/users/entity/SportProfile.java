package co.edu.escuelaing.techcup.users.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents the sports profile of a registered player.
 * Maps to the {@code sport_profiles} table in PostgreSQL.
 *
 * Each player has at most one sports profile linked by their userId
 * from the identity-service. The profile cannot be deleted (TC-13 business rule).
 *
 * Covered requirements:
 *   TC-14 — Consult Sports Profile
 *   TC-17 — Update Sports Profile
 */
@Entity
@Table(name = "sport_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * References the user in identity-service.
     * Unique — one sports profile per player.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    /**
     * Player's preferred field position (TC-13, TC-17).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    /**
     * Player's jersey number (TC-13, TC-17).
     * Uniqueness within a team is enforced at the teams-service level.
     */
    @Column(name = "jersey_number", nullable = false)
    private Integer jerseyNumber;

    /**
     * URL or path to the player's profile photo (TC-17).
     * Null when no photo has been uploaded — the frontend
     * displays the jersey number as avatar in that case.
     */
    @Column(name = "photo_url")
    private String photoUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Enum ──────────────────────────────────────────────────────────────

    public enum Position {
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD
    }
}