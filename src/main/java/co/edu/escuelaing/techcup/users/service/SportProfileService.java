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
import java.util.UUID;

/**
 * Service handling sports profile operations.
 *
 * Covered requirements:
 *   TC-14 — Consult Sports Profile
 *   TC-17 — Update Sports Profile
 *
 * Every operation is recorded in audit_events collection for TC-15.
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

    // ── TC-14 Consult Sports Profile ──────────────────────────────────────

    public SportProfileResponse getProfile(String userId, String requesterId) {
        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sports profile not found for user: " + userId));

        log(requesterId, ActionType.CONSULT_SPORT_PROFILE,
                "Consulted sports profile of user " + userId, ActionResult.SUCCESS);

        return toResponse(profile);
    }

    // ── TC-17 Update Sports Profile ───────────────────────────────────────

    public SportProfileResponse updateProfile(String userId, SportProfileRequest request) {
        // Capture isNew BEFORE save to correctly detect create vs update
        boolean isNew = !sportProfileRepository.existsByUserId(userId);

        SportProfile profile = sportProfileRepository.findByUserId(userId)
                .orElse(SportProfile.builder().userId(userId).build());

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

    // ── Internal helpers ──────────────────────────────────────────────────

    private void log(String userId, ActionType actionType,
                     String description, ActionResult result) {
        auditEventRepository.save(AuditEvent.builder()
                .userId(userId)
                .actionType(actionType)
                .description(description)
                .result(result)
                .build());
    }

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