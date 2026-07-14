package co.edu.escuelaing.techcup.users.repository;

import co.edu.escuelaing.techcup.users.entity.SportProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for SportProfile MongoDB operations.
 */
@Repository
public interface SportProfileRepository extends MongoRepository<SportProfile, String> {

    /**
     * Finds the sports profile linked to a specific user.
     * Used in TC-14 (consult) and TC-17 (update).
     */
    Optional<SportProfile> findByUserId(String userId);

    /**
     * Checks whether a sports profile already exists for a user.
     */
    boolean existsByUserId(String userId);
}