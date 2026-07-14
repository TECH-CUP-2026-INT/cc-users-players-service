package com.techcup.ccusersplayers.service;
import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.exception.BusinessException;
import com.techcup.ccusersplayers.model.SportsProfile;
import com.techcup.ccusersplayers.repository.SportsProfileRepository;
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
public class SportsProfileServiceTest {
    @Mock
    private SportsProfileRepository sportsProfileRepository;
    @InjectMocks
    private SportsProfileService sportsProfileService;
    @Test
    public void testCreateProfile_Success() {
        UUID userId = UUID.randomUUID();
        SportsProfileRequest request = new SportsProfileRequest();
        request.setShirtNumber(10);
        request.setPosition("Forward");
        request.setTeamId(UUID.randomUUID());
        SportsProfile savedProfile = new SportsProfile();
        savedProfile.setId(UUID.randomUUID());
        savedProfile.setUserId(userId);
        savedProfile.setShirtNumber(10);
        savedProfile.setPosition("Forward");
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(sportsProfileRepository.existsByTeamIdAndShirtNumber(any(), any())).thenReturn(false);
        when(sportsProfileRepository.save(any(SportsProfile.class))).thenReturn(savedProfile);
        SportsProfileResponse response = sportsProfileService.createProfile(userId, request);
        assertNotNull(response);
        assertEquals(10, response.getShirtNumber());
        assertEquals("Forward", response.getPosition());
        verify(sportsProfileRepository, times(1)).save(any(SportsProfile.class));
    }
    @Test
    public void testCreateProfile_UserAlreadyHasProfile() {
        UUID userId = UUID.randomUUID();
        SportsProfileRequest request = new SportsProfileRequest();
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(new SportsProfile()));
        assertThrows(BusinessException.class, () -> {
            sportsProfileService.createProfile(userId, request);
        });
        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }
}