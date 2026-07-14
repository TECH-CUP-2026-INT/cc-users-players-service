package co.edu.escuelaing.techcup.users.service;

import co.edu.escuelaing.techcup.users.dto.SportProfileRequest;
import co.edu.escuelaing.techcup.users.dto.SportProfileResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.entity.SportProfile;
import co.edu.escuelaing.techcup.users.entity.SportProfile.Position;
import co.edu.escuelaing.techcup.users.exception.ResourceNotFoundException;
import co.edu.escuelaing.techcup.users.repository.AuditEventRepository;
import co.edu.escuelaing.techcup.users.repository.SportProfileRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SportProfileService.
 *
 * Tests covered:
 *   TC-14 — Consult Sports Profile
 *   TC-17 — Update Sports Profile (create + update flows)
 *   TC-15 — Audit log is recorded on every operation
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SportProfileService — Unit Tests")
class SportProfileServiceTest {

    @Mock private SportProfileRepository sportProfileRepository;
    @Mock private AuditEventRepository   auditEventRepository;

    @InjectMocks
    private SportProfileService sportProfileService;

    // IDs como String — MongoDB usa String como tipo de id
    private final String playerId    = UUID.randomUUID().toString();
    private final String requesterId = UUID.randomUUID().toString();

    private SportProfile existingProfile() {
        return SportProfile.builder()
                .id(UUID.randomUUID().toString())
                .userId(playerId)
                .position(Position.GOALKEEPER)
                .jerseyNumber(1)
                .photoUrl("https://storage/photo.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private SportProfileRequest buildRequest(Position position, Integer jersey, String photoUrl) {
        SportProfileRequest req = new SportProfileRequest();
        req.setPosition(position);
        req.setJerseyNumber(jersey);
        req.setPhotoUrl(photoUrl);
        return req;
    }

    @BeforeEach
    void setupCommonMocks() {
        when(auditEventRepository.save(any(AuditEvent.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC-14 — Consult Sports Profile
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TC-14 — Consult Sports Profile")
    class ConsultSportProfileTests {

        @Test
        @DisplayName("Should return profile when player exists")
        void shouldReturnProfileWhenPlayerExists() {
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));

            SportProfileResponse response = sportProfileService.getProfile(playerId, requesterId);

            assertThat(response).isNotNull();
            assertThat(response.getUserId()).isEqualTo(playerId);
            assertThat(response.getPosition()).isEqualTo(Position.GOALKEEPER);
            assertThat(response.getJerseyNumber()).isEqualTo(1);
            assertThat(response.getPhotoUrl()).isEqualTo("https://storage/photo.jpg");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when player profile does not exist")
        void shouldThrowWhenProfileNotFound() {
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> sportProfileService.getProfile(playerId, requesterId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sports profile not found for user");
        }

        @Test
        @DisplayName("Should record audit event when profile is consulted (TC-15)")
        void shouldRecordAuditEventOnConsult() {
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));

            sportProfileService.getProfile(playerId, requesterId);

            verify(auditEventRepository).save(argThat(event ->
                    event.getActionType() == AuditEvent.ActionType.CONSULT_SPORT_PROFILE &&
                            event.getUserId().equals(requesterId) &&
                            event.getResult() == AuditEvent.ActionResult.SUCCESS
            ));
        }

        @Test
        @DisplayName("Should return profile without photo when photoUrl is null")
        void shouldReturnProfileWithNullPhoto() {
            SportProfile profileNoPhoto = SportProfile.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(playerId)
                    .position(Position.FORWARD)
                    .jerseyNumber(9)
                    .photoUrl(null)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(profileNoPhoto));

            SportProfileResponse response = sportProfileService.getProfile(playerId, requesterId);

            assertThat(response.getPhotoUrl()).isNull();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC-17 — Update Sports Profile
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TC-17 — Update Sports Profile")
    class UpdateSportProfileTests {

        @Test
        @DisplayName("Should create profile when player has none yet")
        void shouldCreateProfileWhenNoneExists() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(false);
            when(sportProfileRepository.findByUserId(playerId)).thenReturn(Optional.empty());
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> {
                        SportProfile p = i.getArgument(0);
                        p.setId(UUID.randomUUID().toString());
                        return p;
                    });

            SportProfileRequest req = buildRequest(Position.MIDFIELDER, 8, null);
            SportProfileResponse response = sportProfileService.updateProfile(playerId, req);

            assertThat(response.getUserId()).isEqualTo(playerId);
            assertThat(response.getPosition()).isEqualTo(Position.MIDFIELDER);
            assertThat(response.getJerseyNumber()).isEqualTo(8);
            verify(sportProfileRepository).save(any(SportProfile.class));
        }

        @Test
        @DisplayName("Should update profile when player already has one")
        void shouldUpdateExistingProfile() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(true);
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> i.getArgument(0));

            SportProfileRequest req = buildRequest(Position.DEFENDER, 5, null);
            SportProfileResponse response = sportProfileService.updateProfile(playerId, req);

            assertThat(response.getPosition()).isEqualTo(Position.DEFENDER);
            assertThat(response.getJerseyNumber()).isEqualTo(5);
            verify(sportProfileRepository).save(any(SportProfile.class));
        }

        @Test
        @DisplayName("Should update photo when photoUrl is provided")
        void shouldUpdatePhotoWhenProvided() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(true);
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> i.getArgument(0));

            SportProfileRequest req = buildRequest(Position.GOALKEEPER, 1, "https://storage/new-photo.jpg");
            SportProfileResponse response = sportProfileService.updateProfile(playerId, req);

            assertThat(response.getPhotoUrl()).isEqualTo("https://storage/new-photo.jpg");
        }

        @Test
        @DisplayName("Should not overwrite photo when photoUrl is null in request")
        void shouldNotOverwritePhotoWhenNull() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(true);
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> i.getArgument(0));

            SportProfileRequest req = buildRequest(Position.FORWARD, 7, null);
            SportProfileResponse response = sportProfileService.updateProfile(playerId, req);

            assertThat(response.getPhotoUrl()).isEqualTo("https://storage/photo.jpg");
        }

        @Test
        @DisplayName("Should record CREATE audit event when profile is new (TC-15)")
        void shouldRecordCreateAuditEvent() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(false);
            when(sportProfileRepository.findByUserId(playerId)).thenReturn(Optional.empty());
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> i.getArgument(0));

            SportProfileRequest req = buildRequest(Position.MIDFIELDER, 10, null);
            sportProfileService.updateProfile(playerId, req);

            verify(auditEventRepository).save(argThat(event ->
                    event.getActionType() == AuditEvent.ActionType.CREATE_SPORT_PROFILE &&
                            event.getUserId().equals(playerId) &&
                            event.getResult() == AuditEvent.ActionResult.SUCCESS
            ));
        }

        @Test
        @DisplayName("Should record UPDATE audit event when profile already exists (TC-15)")
        void shouldRecordUpdateAuditEvent() {
            when(sportProfileRepository.existsByUserId(playerId)).thenReturn(true);
            when(sportProfileRepository.findByUserId(playerId))
                    .thenReturn(Optional.of(existingProfile()));
            when(sportProfileRepository.save(any(SportProfile.class)))
                    .thenAnswer(i -> i.getArgument(0));

            SportProfileRequest req = buildRequest(Position.FORWARD, 9, null);
            sportProfileService.updateProfile(playerId, req);

            verify(auditEventRepository).save(argThat(event ->
                    event.getActionType() == AuditEvent.ActionType.UPDATE_SPORT_PROFILE &&
                            event.getUserId().equals(playerId) &&
                            event.getResult() == AuditEvent.ActionResult.SUCCESS
            ));
        }
    }
}