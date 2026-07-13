package com.techcup.ccusersplayers.service;

import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.exception.BusinessException;
import com.techcup.ccusersplayers.model.SportsProfile;
import com.techcup.ccusersplayers.repository.SportsProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportsProfileServiceTest {

    @Mock
    private SportsProfileRepository sportsProfileRepository;

    @InjectMocks
    private SportsProfileService sportsProfileService;

    private UUID userId;
    private SportsProfileRequest request;
    private SportsProfile profile;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        request = new SportsProfileRequest();
        request.setShirtNumber(10);
        request.setPosition("Forward");
        request.setPhotoUrl("https://example.com/photo.jpg");
        request.setTeamId(UUID.randomUUID());

        profile = new SportsProfile();
        profile.setId(UUID.randomUUID());
        profile.setUserId(userId);
        profile.setShirtNumber(10);
        profile.setPosition("Forward");
        profile.setPhotoUrl("https://example.com/photo.jpg");
        profile.setTeamId(request.getTeamId());
        profile.setIsCaptain(false);
        profile.setIsDeleted(false);
    }

    @Test
    void createProfile_ShouldCreateProfile_WhenValidData() {
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(sportsProfileRepository.existsByTeamIdAndShirtNumber(any(), any())).thenReturn(false);
        when(sportsProfileRepository.save(any(SportsProfile.class))).thenReturn(profile);

        SportsProfileResponse response = sportsProfileService.createProfile(userId, request);

        assertNotNull(response);
        assertEquals(10, response.getShirtNumber());
        assertEquals("Forward", response.getPosition());
        assertEquals("Perfil deportivo creado exitosamente", response.getMessage());
        verify(sportsProfileRepository, times(1)).save(any(SportsProfile.class));
    }

    @Test
    void createProfile_ShouldThrowException_WhenProfileAlreadyExists() {
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        assertThrows(BusinessException.class, () -> {
            sportsProfileService.createProfile(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }

    @Test
    void createProfile_ShouldThrowException_WhenDuplicateShirtNumber() {
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(sportsProfileRepository.existsByTeamIdAndShirtNumber(any(), any())).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            sportsProfileService.createProfile(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }

    @Test
    void createProfile_ShouldThrowException_WhenInvalidPosition() {
        request.setPosition("InvalidPosition");
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            sportsProfileService.createProfile(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }
}