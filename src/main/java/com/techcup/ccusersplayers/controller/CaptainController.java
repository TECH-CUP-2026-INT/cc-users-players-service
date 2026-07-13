package com.techcup.ccusersplayers.controller;

import com.techcup.ccusersplayers.dto.request.CaptainToggleRequest;
import com.techcup.ccusersplayers.dto.response.CaptainToggleResponse;
import com.techcup.ccusersplayers.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class CaptainController {
    
    private final UserService userService;
    
    @PatchMapping("/{userId}/captain")
    public ResponseEntity<CaptainToggleResponse> toggleCaptain(
            @PathVariable UUID userId,
            @Valid @RequestBody CaptainToggleRequest request) {
        
        CaptainToggleResponse response = userService.toggleCaptain(userId, request);
        return ResponseEntity.ok(response);
    }
}