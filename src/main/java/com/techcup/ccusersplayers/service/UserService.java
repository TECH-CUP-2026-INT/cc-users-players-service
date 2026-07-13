package com.techcup.ccusersplayers.service;

import com.techcup.ccusersplayers.dto.request.UserBasicInfoRequest;
import com.techcup.ccusersplayers.dto.request.CaptainToggleRequest;
import com.techcup.ccusersplayers.dto.response.UserBasicInfoResponse;
import com.techcup.ccusersplayers.dto.response.CaptainToggleResponse;
import com.techcup.ccusersplayers.exception.BusinessException;
import com.techcup.ccusersplayers.model.User;
import com.techcup.ccusersplayers.model.SportsProfile;
import com.techcup.ccusersplayers.repository.UserRepository;
import com.techcup.ccusersplayers.repository.SportsProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final SportsProfileRepository sportsProfileRepository;
    
    @Transactional
    public UserBasicInfoResponse updateBasicInfo(UUID userId, UserBasicInfoRequest request) {
        log.info("Actualizando datos básicos del usuario: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        
        if (request.getFullName() != null) {
            user.setName(request.getFullName());
        }
        if (request.getAcademicProgram() != null) {
            user.setAcademicProgram(request.getAcademicProgram());
        }
        if (request.getSemester() != null) {
            user.setSemester(request.getSemester());
        }
        
        User updated = userRepository.save(user);
        log.info("Datos básicos actualizados para usuario: {}", userId);
        
        return new UserBasicInfoResponse(
            updated.getName(),
            updated.getAcademicProgram(),
            updated.getSemester(),
            "Datos básicos actualizados exitosamente"
        );
    }
    
    @Transactional
    public CaptainToggleResponse toggleCaptain(UUID userId, CaptainToggleRequest request) {
        log.info("Toggle capitán para usuario: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        
        SportsProfile profile = sportsProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("El usuario no tiene perfil deportivo"));
        
        if (Boolean.TRUE.equals(request.getActivate())) {
            
            if (profile.getTeamId() == null) {
                throw new BusinessException("Debes pertenecer a un equipo para ser capitán");
            }
            
            boolean existingCaptain = sportsProfileRepository
                .findByTeamIdAndIsCaptainTrue(profile.getTeamId())
                .isPresent();
            
            if (existingCaptain) {
                throw new BusinessException("El equipo ya tiene un capitán");
            }
            
            profile.setIsCaptain(true);
            sportsProfileRepository.save(profile);
            log.info("Usuario {} ahora es capitán", userId);
            
            return new CaptainToggleResponse(
                userId,
                true,
                "¡Felicidades! Ahora eres capitán del equipo"
            );
            
        } else {
            
            if (!Boolean.TRUE.equals(profile.getIsCaptain())) {
                throw new BusinessException("El usuario no es capitán");
            }
            
            profile.setIsCaptain(false);
            sportsProfileRepository.save(profile);
            log.info("Usuario {} ya no es capitán", userId);
            
            return new CaptainToggleResponse(
                userId,
                false,
                "Has dejado de ser capitán"
            );
        }
    }
}