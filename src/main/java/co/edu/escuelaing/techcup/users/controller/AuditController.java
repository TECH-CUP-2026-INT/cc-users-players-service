package co.edu.escuelaing.techcup.users.controller;

import co.edu.escuelaing.techcup.users.dto.AuditEventResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.service.AuditService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for audit log operations.
 * TC-15 — GET /audit/events — Admin only
 */
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<AuditEventResponse>> getEvents(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) String userId,
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