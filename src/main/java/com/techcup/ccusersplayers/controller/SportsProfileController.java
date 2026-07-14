package com.techcup.ccusersplayers.controller;
import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.service.SportsProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController
@RequestMapping("/api/sports-profile")
@RequiredArgsConstructor
@Tag(name = "Sports Profile", description = "Endpoints for managing player sports profiles")
public class SportsProfileController {
    private final SportsProfileService sportsProfileService;
    @Operation(summary = "Create sports profile", description = "Creates a new sports profile for a player")
    @PostMapping
    public ResponseEntity<SportsProfileResponse> createProfile(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody SportsProfileRequest request) {
        SportsProfileResponse response = sportsProfileService.createProfile(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}