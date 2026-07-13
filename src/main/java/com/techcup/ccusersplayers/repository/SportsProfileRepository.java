package com.techcup.ccusersplayers.repository;

import com.techcup.ccusersplayers.model.SportsProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SportsProfileRepository extends JpaRepository<SportsProfile, UUID> {
    
    Optional<SportsProfile> findByUserId(UUID userId);
    
    boolean existsByTeamIdAndShirtNumber(UUID teamId, Integer shirtNumber);
    
    Optional<SportsProfile> findByUserIdAndTeamId(UUID userId, UUID teamId);
    
    Optional<SportsProfile> findByTeamIdAndIsCaptainTrue(UUID teamId);
}