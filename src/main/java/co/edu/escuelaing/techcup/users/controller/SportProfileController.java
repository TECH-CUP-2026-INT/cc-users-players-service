package co.edu.escuelaing.techcup.users.controller;

import co.edu.escuelaing.techcup.users.dto.SportProfileRequest;
import co.edu.escuelaing.techcup.users.dto.SportProfileResponse;
import co.edu.escuelaing.techcup.users.service.SportProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for sports profile operations.
 *
 * JWT validation is handled upstream by the API Gateway.
 * The userId of the authenticated user is forwarded by the Gateway
 * in the X-User-Id header on every request.
 *
 * TC-14 — GET  /players/{userId}/profile
 * TC-17 — PUT  /players/{userId}/profile
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
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<SportProfileResponse> getProfile(
            @PathVariable String userId,
            @RequestHeader("X-User-Id") String requesterId) {

        return ResponseEntity.ok(sportProfileService.getProfile(userId, requesterId));
    }

    /**
     * TC-17 — Update Sports Profile.
     * Only the player themselves can update their own profile.
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<SportProfileResponse> updateProfile(
            @PathVariable String userId,
            @RequestHeader("X-User-Id") String requesterId,
            @Valid @RequestBody SportProfileRequest request) {

        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(sportProfileService.updateProfile(userId, request));
    }
}