package co.edu.escuelaing.techcup.users.controller;

import co.edu.escuelaing.techcup.users.config.TestSecurityConfig;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.entity.SportProfile;
import co.edu.escuelaing.techcup.users.entity.SportProfile.Position;
import co.edu.escuelaing.techcup.users.repository.AuditEventRepository;
import co.edu.escuelaing.techcup.users.repository.SportProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for SportProfileController and AuditController.
 * Uses H2 in-memory database and disables Spring Security.
 *
 * Tests covered:
 *   TC-14 — GET  /players/{userId}/profile
 *   TC-15 — GET  /audit/events
 *   TC-17 — PUT  /players/{userId}/profile
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@DisplayName("SportProfile & Audit Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SportProfileControllerIntegrationTest {

    @Autowired private MockMvc                 mockMvc;
    @Autowired private SportProfileRepository  sportProfileRepository;
    @Autowired private AuditEventRepository    auditEventRepository;
    @Autowired private ObjectMapper            objectMapper;

    private final UUID playerId    = UUID.randomUUID();
    private final UUID requesterId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        auditEventRepository.deleteAll();
        sportProfileRepository.deleteAll();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private SportProfile savedProfile() {
        return sportProfileRepository.save(
                SportProfile.builder()
                        .userId(playerId)
                        .position(Position.GOALKEEPER)
                        .jerseyNumber(1)
                        .photoUrl("https://storage/photo.jpg")
                        .build()
        );
    }

    private String updateRequestBody(String position, int jersey, String photoUrl) {
        return String.format("""
                {
                  "position": "%s",
                  "jerseyNumber": %d,
                  "photoUrl": %s
                }
                """, position, jersey,
                photoUrl != null ? "\"" + photoUrl + "\"" : "null");
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC-14 — Consult Sports Profile
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TC-14 — GET /players/{userId}/profile")
    class ConsultProfileTests {

        @Test
        @Order(1)
        @DisplayName("Should return 200 with profile when player exists")
        void shouldReturn200WithProfile() throws Exception {
            savedProfile();

            mockMvc.perform(get("/players/{userId}/profile", playerId)
                            .header("X-User-Id", requesterId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(playerId.toString()))
                    .andExpect(jsonPath("$.position").value("GOALKEEPER"))
                    .andExpect(jsonPath("$.jerseyNumber").value(1))
                    .andExpect(jsonPath("$.photoUrl").value("https://storage/photo.jpg"));
        }

        @Test
        @Order(2)
        @DisplayName("Should return 404 when player has no profile")
        void shouldReturn404WhenProfileNotFound() throws Exception {
            mockMvc.perform(get("/players/{userId}/profile", UUID.randomUUID())
                            .header("X-User-Id", requesterId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            containsString("Sports profile not found")));
        }

        @Test
        @Order(3)
        @DisplayName("Should record audit event when profile is consulted (TC-15)")
        void shouldRecordAuditEventOnConsult() throws Exception {
            savedProfile();

            mockMvc.perform(get("/players/{userId}/profile", playerId)
                            .header("X-User-Id", requesterId))
                    .andExpect(status().isOk());

            long auditCount = auditEventRepository.findAll().stream()
                    .filter(e -> e.getActionType() == AuditEvent.ActionType.CONSULT_SPORT_PROFILE)
                    .count();
            org.assertj.core.api.Assertions.assertThat(auditCount).isEqualTo(1);
        }

        @Test
        @Order(4)
        @DisplayName("Should return 400 when X-User-Id header is missing")
        void shouldReturn400WhenHeaderMissing() throws Exception {
            mockMvc.perform(get("/players/{userId}/profile", playerId))
                    .andExpect(status().isBadRequest());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC-17 — Update Sports Profile
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TC-17 — PUT /players/{userId}/profile")
    class UpdateProfileTests {

        @Test
        @Order(1)
        @DisplayName("Should return 200 and create profile when player has none")
        void shouldCreateProfileWhenNoneExists() throws Exception {
            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateRequestBody("MIDFIELDER", 8, null)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(playerId.toString()))
                    .andExpect(jsonPath("$.position").value("MIDFIELDER"))
                    .andExpect(jsonPath("$.jerseyNumber").value(8));
        }

        @Test
        @Order(2)
        @DisplayName("Should return 200 and update existing profile")
        void shouldUpdateExistingProfile() throws Exception {
            savedProfile();

            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateRequestBody("FORWARD", 9, "https://storage/new.jpg")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.position").value("FORWARD"))
                    .andExpect(jsonPath("$.jerseyNumber").value(9))
                    .andExpect(jsonPath("$.photoUrl").value("https://storage/new.jpg"));
        }

        @Test
        @Order(3)
        @DisplayName("Should return 403 when requester is not the profile owner")
        void shouldReturn403WhenNotOwner() throws Exception {
            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateRequestBody("DEFENDER", 4, null)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @Order(4)
        @DisplayName("Should return 400 when position is missing")
        void shouldReturn400WhenPositionMissing() throws Exception {
            String body = """
                    {
                      "jerseyNumber": 5
                    }
                    """;

            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(5)
        @DisplayName("Should return 400 when jersey number is missing")
        void shouldReturn400WhenJerseyMissing() throws Exception {
            String body = """
                    {
                      "position": "DEFENDER"
                    }
                    """;

            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(6)
        @DisplayName("Should record CREATE audit event when profile is new (TC-15)")
        void shouldRecordCreateAuditEvent() throws Exception {
            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateRequestBody("GOALKEEPER", 1, null)))
                    .andExpect(status().isOk());

            long count = auditEventRepository.findAll().stream()
                    .filter(e -> e.getActionType() == AuditEvent.ActionType.CREATE_SPORT_PROFILE)
                    .count();
            org.assertj.core.api.Assertions.assertThat(count).isEqualTo(1);
        }

        @Test
        @Order(7)
        @DisplayName("Should record UPDATE audit event when profile already exists (TC-15)")
        void shouldRecordUpdateAuditEvent() throws Exception {
            savedProfile();

            mockMvc.perform(put("/players/{userId}/profile", playerId)
                            .header("X-User-Id", playerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateRequestBody("DEFENDER", 3, null)))
                    .andExpect(status().isOk());

            long count = auditEventRepository.findAll().stream()
                    .filter(e -> e.getActionType() == AuditEvent.ActionType.UPDATE_SPORT_PROFILE)
                    .count();
            org.assertj.core.api.Assertions.assertThat(count).isEqualTo(1);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC-15 — Consult Audit Events
    // ══════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TC-15 — GET /audit/events")
    class AuditEventsTests {

        private void seedAuditEvent(AuditEvent.ActionType type) {
            auditEventRepository.save(AuditEvent.builder()
                    .userId(playerId)
                    .actionType(type)
                    .description("Test event")
                    .result(AuditEvent.ActionResult.SUCCESS)
                    .build());
        }

        @Test
        @Order(1)
        @DisplayName("Should return 200 with all events when Admin requests")
        void shouldReturn200ForAdmin() throws Exception {
            seedAuditEvent(AuditEvent.ActionType.CONSULT_SPORT_PROFILE);
            seedAuditEvent(AuditEvent.ActionType.UPDATE_SPORT_PROFILE);

            mockMvc.perform(get("/audit/events")
                            .header("X-User-Role", "ADMIN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @Order(2)
        @DisplayName("Should return 403 when non-Admin requests")
        void shouldReturn403ForNonAdmin() throws Exception {
            mockMvc.perform(get("/audit/events")
                            .header("X-User-Role", "PLAYER"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @Order(3)
        @DisplayName("Should return 403 when X-User-Role is missing")
        void shouldReturn400WhenRoleHeaderMissing() throws Exception {
            mockMvc.perform(get("/audit/events"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(4)
        @DisplayName("Should filter events by userId query param")
        void shouldFilterByUserId() throws Exception {
            seedAuditEvent(AuditEvent.ActionType.CONSULT_SPORT_PROFILE);
            auditEventRepository.save(AuditEvent.builder()
                    .userId(UUID.randomUUID())
                    .actionType(AuditEvent.ActionType.UPDATE_SPORT_PROFILE)
                    .description("Other user event")
                    .result(AuditEvent.ActionResult.SUCCESS)
                    .build());

            mockMvc.perform(get("/audit/events")
                            .header("X-User-Role", "ADMIN")
                            .param("userId", playerId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].userId").value(playerId.toString()));
        }

        @Test
        @Order(5)
        @DisplayName("Should return empty list when no events exist")
        void shouldReturnEmptyListWhenNoEvents() throws Exception {
            mockMvc.perform(get("/audit/events")
                            .header("X-User-Role", "ADMIN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}