package co.edu.escuelaing.techcup.users.repository;

import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for AuditEvent persistence operations.
 *
 * @see AuditEvent
 */
@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, UUID> {

    /**
     * Finds all audit events for a specific user.
     * TC-15 filter: by userId.
     */
    List<AuditEvent> findByUserId(UUID userId);

    /**
     * Finds all audit events of a specific action type.
     * TC-15 filter: by actionType.
     */
    List<AuditEvent> findByActionType(AuditEvent.ActionType actionType);

    /**
     * Finds all audit events within a date range.
     * TC-15 filter: by date range.
     */
    List<AuditEvent> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Finds all audit events filtered by userId and date range.
     * TC-15 combined filter.
     */
    List<AuditEvent> findByUserIdAndTimestampBetween(UUID userId,
                                                     LocalDateTime from,
                                                     LocalDateTime to);
}