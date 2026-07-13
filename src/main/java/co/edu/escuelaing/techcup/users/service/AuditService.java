package co.edu.escuelaing.techcup.users.service;

import co.edu.escuelaing.techcup.users.dto.AuditEventResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.repository.AuditEventRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for querying audit log entries.
 *
 * Covered requirements:
 *   TC-15 — Consult Users and Players Service Events (Admin only)
 *
 * Access control (Admin role check) is enforced at the controller level.
 */
@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    public AuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    /**
     * Returns audit events filtered by the provided parameters.
     * All parameters are optional — if none are provided, returns all events.
     *
     * @param userId     filter by user UUID (optional)
     * @param actionType filter by action type (optional)
     * @param from       start of date range (optional)
     * @param to         end of date range (optional)
     * @return list of matching audit events
     */
    public List<AuditEventResponse> getEvents(UUID userId,
                                              AuditEvent.ActionType actionType,
                                              LocalDateTime from,
                                              LocalDateTime to) {
        List<AuditEvent> events;

        if (userId != null && from != null && to != null) {
            events = auditEventRepository.findByUserIdAndTimestampBetween(userId, from, to);
        } else if (userId != null) {
            events = auditEventRepository.findByUserId(userId);
        } else if (actionType != null) {
            events = auditEventRepository.findByActionType(actionType);
        } else if (from != null && to != null) {
            events = auditEventRepository.findByTimestampBetween(from, to);
        } else {
            events = auditEventRepository.findAll();
        }

        return events.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AuditEventResponse toResponse(AuditEvent event) {
        return AuditEventResponse.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .actionType(event.getActionType())
                .description(event.getDescription())
                .result(event.getResult())
                .timestamp(event.getTimestamp())
                .build();
    }
}