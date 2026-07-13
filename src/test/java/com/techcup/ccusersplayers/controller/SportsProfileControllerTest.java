package com.techcup.ccusersplayers.controller;

import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.service.SportsProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportsProfileControllerTest {

    @Mock
    private SportsProfileService sportsProfileService;

    @InjectMocks
    private SportsProfileController sportsProfileController;

    private UUID userId;
    private SportsProfileRequest request;
    private SportsProfileResponse response;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        request = new SportsProfileRequest();
        request.setShirtNumber(10);
        request.setPosition("Forward");
        request.setPhotoUrl("https://example.com/photo.jpg");

        response = new SportsProfileResponse(
            UUID.randomUUID(),
            userId,
            10,
            "Forward",
            "https://example.com/photo.jpg",
            null,
            false,
            "Perfil deportivo creado exitosamente"
        );
    }

    @Test
    void createProfile_ShouldReturnCreated_WhenValidRequest() {
        when(sportsProfileService.createProfile(any(UUID.class), any(SportsProfileRequest.class)))
            .thenReturn(response);

        ResponseEntity<SportsProfileResponse> result = sportsProfileController.createProfile(userId, request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(sportsProfileService, times(1)).createProfile(any(UUID.class), any(SportsProfileRequest.class));
    }
}