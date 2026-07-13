package com.techcup.ccusersplayers.service;

import com.techcup.ccusersplayers.dto.request.SportsProfileRequest;
import com.techcup.ccusersplayers.dto.response.SportsProfileResponse;
import com.techcup.ccusersplayers.exception.BusinessException;
import com.techcup.ccusersplayers.model.SportsProfile;
import com.techcup.ccusersplayers.repository.SportsProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportsProfileService {
    private final SportsProfileRepository sportsProfileRepository;
    @Transactional
    public SportsProfileResponse createProfile(UUID userId, SportsProfileRequest request) {
        log.info("Creando perfil deportivo para usuario: {}", userId);
        if (sportsProfileRepository.findByUserId(userId).isPresent()) {
            throw new BusinessException("El usuario ya tiene un perfil deportivo");
        }
        if (request.getTeamId() != null) {
            boolean dorsalExists = sportsProfileRepository
                .existsByTeamIdAndShirtNumber(request.getTeamId(), request.getShirtNumber());
            if (dorsalExists) {
                throw new BusinessException("El dorsal " + request.getShirtNumber() + 
                                          " ya está ocupado en este equipo");
            }
        }
        String position = request.getPosition();
        if (!position.matches("Goalkeeper|Defender|Midfielder|Forward")) {
            throw new BusinessException("Posición inválida. Debe ser: Goalkeeper, Defender, Midfielder o Forward");
        }
        SportsProfile profile = new SportsProfile();
        profile.setUserId(userId);
        profile.setShirtNumber(request.getShirtNumber());
        profile.setPosition(request.getPosition());
        profile.setPhotoUrl(request.getPhotoUrl());
        profile.setTeamId(request.getTeamId());
        profile.setIsCaptain(false);
        profile.setIsDeleted(false);
        SportsProfile saved = sportsProfileRepository.save(profile);
        log.info("Perfil deportivo creado exitosamente con ID: {}", saved.getId());
        return new SportsProfileResponse(
            saved.getId(),
            saved.getUserId(),
            saved.getShirtNumber(),
            saved.getPosition(),
            saved.getPhotoUrl(),
            saved.getTeamId(),
            saved.getIsCaptain(),
            "Perfil deportivo creado exitosamente"
        );
    }
}