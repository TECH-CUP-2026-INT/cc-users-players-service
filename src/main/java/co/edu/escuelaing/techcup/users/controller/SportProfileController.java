package co.edu.escuelaing.techcup.users.controller;

import co.edu.escuelaing.techcup.users.dto.SportProfileRequest;
import co.edu.escuelaing.techcup.users.dto.SportProfileResponse;
import co.edu.escuelaing.techcup.users.service.SportProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * REST controller for sports profile operations.
 *
 * JWT validation is handled upstream by the API Gateway.
 * The userId of the authenticated user is forwarded by the Gateway
 * in the {@code X-User-Id} header on every request.
 *
 * Covered requirements:
 *   TC-14 — GET  /players/{userId}/profile — Consult Sports Profile
 *   TC-17 — PUT  /players/{userId}/profile — Update Sports Profile
 */
@RestController
@RequestMapping("/players")
public class SportProfileController {

    private final SportProfileService sportProfileService;

    public SportProfileController(SportProfileService sportProfileService) {
        this.sportProfileService = sportProfileService;
    }

    /**
     * TC-14 — Consult Sports Profile.
     * Any authenticated user can view any player's sports profile.
     *
     * @param userId      path variable — UUID of the player to consult
     * @param requesterId X-User-Id header — UUID of the user making the request (for audit)
     * @return the player's sports profile
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<SportProfileResponse> getProfile(
            @PathVariable UUID userId,
            @RequestHeader("X-User-Id") UUID requesterId) {

        return ResponseEntity.ok(sportProfileService.getProfile(userId, requesterId));
    }

    /**
     * TC-17 — Update Sports Profile.
     * Only the player themselves can update their own profile.
     * The X-User-Id header must match the userId path variable.
     *
     * @param userId  path variable — UUID of the player
     * @param request updated profile data
     * @return the updated sports profile
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<SportProfileResponse> updateProfile(
            @PathVariable UUID userId,
            @RequestHeader("X-User-Id") UUID requesterId,
            @Valid @RequestBody SportProfileRequest request) {

        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(sportProfileService.updateProfile(userId, request));
    }
}