package co.edu.escuelaing.techcup.users.repository;

import co.edu.escuelaing.techcup.users.entity.SportProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for SportProfile persistence operations.
 *
 * @see SportProfile
 */
@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, UUID> {

    /**
     * Finds the sports profile linked to a specific user.
     * Used in TC-14 (consult) and TC-17 (update).
     *
     * @param userId the UUID from identity-service
     * @return an Optional containing the profile if found
     */
    Optional<SportProfile> findByUserId(UUID userId);

    /**
     * Checks whether a sports profile already exists for a user.
     * Used to distinguish between create and update flows.
     *
     * @param userId the UUID from identity-service
     * @return true if a profile exists for that user
     */
    boolean existsByUserId(UUID userId);
}