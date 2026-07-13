package co.edu.escuelaing.techcup.users.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an audit log entry for actions performed in the users-players-service.
 * Maps to the {@code audit_events} table in PostgreSQL.
 *
 * Covered requirements:
 *   TC-15 — Consult Users and Players Service Events (Admin only)
 */
@Entity
@Table(name = "audit_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * UUID of the user who triggered the action.
     * References the user in identity-service.
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Type of action performed.
     * E.g. CREATE_SPORT_PROFILE, UPDATE_SPORT_PROFILE, UPDATE_PHOTO.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    /**
     * Human-readable description of the action for audit purposes.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Result of the action.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionResult result;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    // ── Enums ──────────────────────────────────────────────────────────────

    public enum ActionType {
        CREATE_SPORT_PROFILE,
        UPDATE_SPORT_PROFILE,
        UPDATE_PHOTO,
        CONSULT_SPORT_PROFILE
    }

    public enum ActionResult {
        SUCCESS,
        FAILURE
    }
}