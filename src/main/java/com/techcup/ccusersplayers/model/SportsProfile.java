package com.techcup.ccusersplayers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sports_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportsProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
    @Column(name = "shirt_number", nullable = false)
    private Integer shirtNumber;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "team_id")
    private UUID teamId;
    @Column(name = "is_captain", nullable = false)
    private Boolean isCaptain = false;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}