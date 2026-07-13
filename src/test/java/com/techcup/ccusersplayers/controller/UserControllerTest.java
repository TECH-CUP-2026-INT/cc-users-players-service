package com.techcup.ccusersplayers.controller;

import com.techcup.ccusersplayers.dto.request.UserBasicInfoRequest;
import com.techcup.ccusersplayers.dto.response.UserBasicInfoResponse;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserBasicInfoRequest request;
    private UserBasicInfoResponse response;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        request = new UserBasicInfoRequest();
        request.setFullName("Juan Carlos Pérez");
        request.setAcademicProgram("Ingeniería de Software");
        request.setSemester(6);

        response = new UserBasicInfoResponse(
            "Juan Carlos Pérez",
            "Ingeniería de Software",
            6,
            "Datos básicos actualizados exitosamente"
        );
    }

    @Test
    void updateBasicInfo_ShouldReturnOk_WhenValidRequest() {
        when(userService.updateBasicInfo(any(UUID.class), any(UserBasicInfoRequest.class)))
            .thenReturn(response);

        ResponseEntity<UserBasicInfoResponse> result = userController.updateBasicInfo(userId, request);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(userService, times(1)).updateBasicInfo(any(UUID.class), any(UserBasicInfoRequest.class));
    }
}