package com.techcup.ccusersplayers.controller;
import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.service.SportsProfileService;
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
public class SportsProfileControllerTest {
    @Mock
    private SportsProfileService sportsProfileService;
    @InjectMocks
    private SportsProfileController sportsProfileController;
    @Test
    public void testCreateProfile() {
        UUID userId = UUID.randomUUID();
        SportsProfileRequest request = new SportsProfileRequest();
        request.setShirtNumber(10);
        request.setPosition("Forward");
        SportsProfileResponse response = new SportsProfileResponse();
        response.setUserId(userId);
        response.setShirtNumber(10);
        response.setPosition("Forward");
        when(sportsProfileService.createProfile(any(UUID.class), any(SportsProfileRequest.class)))
            .thenReturn(response);
        ResponseEntity<SportsProfileResponse> result = sportsProfileController.createProfile(userId, request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(10, result.getBody().getShirtNumber());
        verify(sportsProfileService, times(1)).createProfile(any(UUID.class), any(SportsProfileRequest.class));
    }
}