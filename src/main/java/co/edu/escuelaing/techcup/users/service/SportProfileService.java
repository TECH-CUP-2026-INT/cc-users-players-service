package co.edu.escuelaing.techcup.users.service;

import co.edu.escuelaing.techcup.users.dto.SportProfileRequest;
import co.edu.escuelaing.techcup.users.dto.SportProfileResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionType;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionResult;
import co.edu.escuelaing.techcup.users.entity.SportProfile;
import co.edu.escuelaing.techcup.users.exception.ResourceNotFoundException;
import co.edu.escuelaing.techcup.users.repository.AuditEventRepository;
import co.edu.escuelaing.techcup.users.repository.SportProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/**
 * Service handling sports profile operations.
 *
 * Covered requirements:
 *   TC-14 — Consult Sports Profile
 *   TC-17 — Update Sports Profile
 *
 * Every operation is recorded in {@code audit_events} for TC-15.
 * The API Gateway is responsible for JWT validation — this service
 * receives the userId extracted from the token as a request header.
 */
@Service
public class SportProfileService {

    private final SportProfileRepository sportProfileRepository;
    private final AuditEventRepository   auditEventRepository;

    public SportProfileService(SportProfileRepository sportProfileRepository,
                               AuditEventRepository auditEventRepository) {
        this.sportProfileRepository = sportProfileRepository;
        this.auditEventRepository   = auditEventRepository;
    }

    //TC-14 Consult Sports Profile

    /**
     * Returns the sports profile of a player identified by their userId.
     * Any authenticated user can consult any player's profile (TC-14).
     *
     * @param userId    UUID of the player from identity-service
     * @param requesterId UUID of the user making the request (for audit)
     * @return the player's sports profile data
     */
    public SportProfileResponse getProfile(UUID userId, UUID requesterId) {
        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sports profile not found for user: " + userId));

        log(requesterId, ActionType.CONSULT_SPORT_PROFILE,
                "Consulted sports profile of user " + userId, ActionResult.SUCCESS);

        return toResponse(profile);
    }

    // TC-17 Update Sports Profile

    /**
     * Updates an existing sports profile.
     * If no profile exists yet for the userId, creates one (TC-13 flow).
     * The tournament-active restriction is enforced at the teams-service level.
     *
     * @param userId  UUID of the player from identity-service
     * @param request updated profile data
     * @return the updated sports profile
     */
    @Transactional
    public SportProfileResponse updateProfile(UUID userId, SportProfileRequest request) {
        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .orElse(SportProfile.builder().userId(userId).build());

        // ← Capturar si es nuevo ANTES del save
        boolean isNew = profile.getId() == null;

        profile.setPosition(request.getPosition());
        profile.setJerseyNumber(request.getJerseyNumber());

        if (request.getPhotoUrl() != null) {
            profile.setPhotoUrl(request.getPhotoUrl());
        }

        SportProfile saved = sportProfileRepository.save(profile);

        ActionType actionType = isNew
                ? ActionType.CREATE_SPORT_PROFILE
                : ActionType.UPDATE_SPORT_PROFILE;

        log(userId, actionType,
                (isNew ? "Created" : "Updated") + " sports profile for user " + userId,
                ActionResult.SUCCESS);

        return toResponse(saved);
    }

    // Internal helpers

    /**
     * Persists an audit event entry for TC-15.
     */
    private void log(UUID userId, ActionType actionType,
                     String description, ActionResult result) {
        auditEventRepository.save(AuditEvent.builder()
                .userId(userId)
                .actionType(actionType)
                .description(description)
                .result(result)
                .build());
    }

    /**
     * Maps a SportProfile entity to its response DTO.
     */
    private SportProfileResponse toResponse(SportProfile profile) {
        return SportProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .position(profile.getPosition())
                .jerseyNumber(profile.getJerseyNumber())
                .photoUrl(profile.getPhotoUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}