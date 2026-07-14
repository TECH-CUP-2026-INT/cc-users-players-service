package co.edu.escuelaing.techcup.users.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Represents an audit log entry for actions performed in the users-players-service.
 * Stored in the {@code audit_events} MongoDB collection.
 *
 * Covered requirements:
 *   TC-15 — Consult Users and Players Service Events (Admin only)
 */
@Document(collection = "audit_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    @Id
    private String id;

    /**
     * References the user in identity-service.
     */
    private String userId;

    /**
     * Type of action performed.
     */
    private ActionType actionType;

    /**
     * Human-readable description of the action.
     */
    private String description;

    /**
     * Result of the action.
     */
    private ActionResult result;

    @CreatedDate
    private LocalDateTime timestamp;

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