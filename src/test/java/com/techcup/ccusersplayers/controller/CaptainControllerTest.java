package com.techcup.ccusersplayers.controller;

import com.techcup.ccusersplayers.dto.request.CaptainToggleRequest;
import com.techcup.ccusersplayers.dto.response.CaptainToggleResponse;
import com.techcup.ccusersplayers.service.UserService;
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
class CaptainControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CaptainController captainController;

    private UUID userId;
    private CaptainToggleRequest request;
    private CaptainToggleResponse response;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        request = new CaptainToggleRequest();
        request.setActivate(true);

        response = new CaptainToggleResponse(
            userId,
            true,
            "¡Felicidades! Ahora eres capitán del equipo"
        );
    }

    @Test
    void toggleCaptain_ShouldReturnOk_WhenValidRequest() {
        when(userService.toggleCaptain(any(UUID.class), any(CaptainToggleRequest.class)))
            .thenReturn(response);

        ResponseEntity<CaptainToggleResponse> result = captainController.toggleCaptain(userId, request);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(userService, times(1)).toggleCaptain(any(UUID.class), any(CaptainToggleRequest.class));
    }
}