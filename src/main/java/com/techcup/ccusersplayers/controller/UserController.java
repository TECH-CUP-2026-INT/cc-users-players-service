package com.techcup.ccusersplayers.controller;

import com.techcup.ccusersplayers.dto.request.UserBasicInfoRequest;
import com.techcup.ccusersplayers.dto.response.UserBasicInfoResponse;
import com.techcup.ccusersplayers.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PatchMapping("/{userId}/basic-info")
    public ResponseEntity<UserBasicInfoResponse> updateBasicInfo(
            @PathVariable UUID userId,
            @Valid @RequestBody UserBasicInfoRequest request) {
        
        UserBasicInfoResponse response = userService.updateBasicInfo(userId, request);
        return ResponseEntity.ok(response);
    }
}