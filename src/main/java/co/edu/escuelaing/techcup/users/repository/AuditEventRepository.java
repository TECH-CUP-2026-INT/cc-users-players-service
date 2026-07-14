package co.edu.escuelaing.techcup.users.repository;

import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for AuditEvent MongoDB operations.
 */
@Repository
public interface AuditEventRepository extends MongoRepository<AuditEvent, String> {

    List<AuditEvent> findByUserId(String userId);

    List<AuditEvent> findByActionType(ActionType actionType);

    List<AuditEvent> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    List<AuditEvent> findByUserIdAndTimestampBetween(String userId,
                                                     LocalDateTime from,
                                                     LocalDateTime to);
}