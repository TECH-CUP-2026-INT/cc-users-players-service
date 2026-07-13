package com.techcup.ccusersplayers.service;

import com.techcup.ccusersplayers.dto.request.CaptainToggleRequest;
import com.techcup.ccusersplayers.dto.request.UserBasicInfoRequest;
import com.techcup.ccusersplayers.dto.response.CaptainToggleResponse;
import com.techcup.ccusersplayers.dto.response.UserBasicInfoResponse;
import com.techcup.ccusersplayers.exception.BusinessException;
import com.techcup.ccusersplayers.model.SportsProfile;
import com.techcup.ccusersplayers.model.User;
import com.techcup.ccusersplayers.repository.SportsProfileRepository;
import com.techcup.ccusersplayers.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SportsProfileRepository sportsProfileRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private SportsProfile profile;
    private UUID teamId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        teamId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Juan Pérez");
        user.setEmail("juan@universidad.edu");
        user.setAcademicProgram("Ingeniería de Sistemas");
        user.setSemester(5);

        profile = new SportsProfile();
        profile.setId(UUID.randomUUID());
        profile.setUserId(userId);
        profile.setShirtNumber(10);
        profile.setPosition("Forward");
        profile.setTeamId(teamId);
        profile.setIsCaptain(false);
    }

    @Test
    void updateBasicInfo_ShouldUpdateUser_WhenValidData() {
        UserBasicInfoRequest request = new UserBasicInfoRequest();
        request.setFullName("Juan Carlos Pérez");
        request.setAcademicProgram("Ingeniería de Software");
        request.setSemester(6);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserBasicInfoResponse response = userService.updateBasicInfo(userId, request);

        assertNotNull(response);
        assertEquals("Juan Carlos Pérez", response.getFullName());
        assertEquals("Ingeniería de Software", response.getAcademicProgram());
        assertEquals(6, response.getSemester());
        assertEquals("Datos básicos actualizados exitosamente", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateBasicInfo_ShouldThrowException_WhenUserNotFound() {
        UserBasicInfoRequest request = new UserBasicInfoRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            userService.updateBasicInfo(userId, request);
        });
    }

    @Test
    void toggleCaptain_ShouldActivateCaptain_WhenValid() {
        CaptainToggleRequest request = new CaptainToggleRequest();
        request.setActivate(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(sportsProfileRepository.findByTeamIdAndIsCaptainTrue(teamId)).thenReturn(Optional.empty());
        when(sportsProfileRepository.save(any(SportsProfile.class))).thenReturn(profile);

        CaptainToggleResponse response = userService.toggleCaptain(userId, request);

        assertNotNull(response);
        assertTrue(response.getIsCaptain());
        assertEquals("¡Felicidades! Ahora eres capitán del equipo", response.getMessage());
        verify(sportsProfileRepository, times(1)).save(any(SportsProfile.class));
    }

    @Test
    void toggleCaptain_ShouldThrowException_WhenUserNotInTeam() {
        CaptainToggleRequest request = new CaptainToggleRequest();
        request.setActivate(true);
        profile.setTeamId(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        assertThrows(BusinessException.class, () -> {
            userService.toggleCaptain(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }

    @Test
    void toggleCaptain_ShouldThrowException_WhenTeamAlreadyHasCaptain() {
        CaptainToggleRequest request = new CaptainToggleRequest();
        request.setActivate(true);
        
        SportsProfile existingCaptain = new SportsProfile();
        existingCaptain.setIsCaptain(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(sportsProfileRepository.findByTeamIdAndIsCaptainTrue(teamId)).thenReturn(Optional.of(existingCaptain));

        assertThrows(BusinessException.class, () -> {
            userService.toggleCaptain(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }

    @Test
    void toggleCaptain_ShouldDeactivateCaptain_WhenValid() {
        CaptainToggleRequest request = new CaptainToggleRequest();
        request.setActivate(false);
        profile.setIsCaptain(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(sportsProfileRepository.save(any(SportsProfile.class))).thenReturn(profile);

        CaptainToggleResponse response = userService.toggleCaptain(userId, request);

        assertNotNull(response);
        assertFalse(response.getIsCaptain());
        assertEquals("Has dejado de ser capitán", response.getMessage());
        verify(sportsProfileRepository, times(1)).save(any(SportsProfile.class));
    }

    @Test
    void toggleCaptain_ShouldThrowException_WhenNotCaptain() {
        CaptainToggleRequest request = new CaptainToggleRequest();
        request.setActivate(false);
        profile.setIsCaptain(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sportsProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        assertThrows(BusinessException.class, () -> {
            userService.toggleCaptain(userId, request);
        });

        verify(sportsProfileRepository, never()).save(any(SportsProfile.class));
    }
}