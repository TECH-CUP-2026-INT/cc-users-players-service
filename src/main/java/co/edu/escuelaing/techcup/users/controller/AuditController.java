package co.edu.escuelaing.techcup.users.controller;

import co.edu.escuelaing.techcup.users.dto.AuditEventResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.service.AuditService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for audit log operations.
 *
 * Access is restricted to Admin role only (TC-15).
 * The API Gateway validates the JWT and forwards the user role
 * in the {@code X-User-Role} header. Requests without ADMIN role are rejected.
 *
 * Covered requirements:
 *   TC-15 — GET /audit/events — Consult Users and Players Service Events
 */
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * TC-15 — Consult Users and Players Service Events.
     * All query parameters are optional — if none provided, returns all events.
     *
     * @param userRole    X-User-Role header forwarded by API Gateway (must be ADMIN)
     * @param userId      optional filter by user UUID
     * @param actionType  optional filter by action type
     * @param from        optional start of date range (ISO format)
     * @param to          optional end of date range (ISO format)
     * @return list of audit events matching the filters
     */
    @GetMapping("/events")
    public ResponseEntity<List<AuditEventResponse>> getEvents(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) AuditEvent.ActionType actionType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(auditService.getEvents(userId, actionType, from, to));
    }
}