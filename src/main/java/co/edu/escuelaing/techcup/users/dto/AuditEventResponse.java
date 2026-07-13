package co.edu.escuelaing.techcup.users.dto;

import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionResult;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for audit log entries.
 * Returned by TC-15 (Consult Users and Players Service Events).
 */
@Data
@Builder
public class AuditEventResponse {

    private UUID         id;
    private UUID         userId;
    private ActionType   actionType;
    private String       description;
    private ActionResult result;
    private LocalDateTime timestamp;
}