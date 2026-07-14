package co.edu.escuelaing.techcup.users.dto;

import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionResult;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Response DTO for audit log entries.
 */
@Data
@Builder
public class AuditEventResponse {

    private String       id;
    private String       userId;
    private ActionType   actionType;
    private String       description;
    private ActionResult result;
    private LocalDateTime timestamp;
}