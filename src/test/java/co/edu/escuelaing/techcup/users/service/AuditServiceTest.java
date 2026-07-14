package co.edu.escuelaing.techcup.users.service;

import co.edu.escuelaing.techcup.users.dto.AuditEventResponse;
import co.edu.escuelaing.techcup.users.entity.AuditEvent;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionType;
import co.edu.escuelaing.techcup.users.entity.AuditEvent.ActionResult;
import co.edu.escuelaing.techcup.users.repository.AuditEventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuditService.
 * TC-15 — Consult Users and Players Service Events
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AuditService — Unit Tests (TC-15)")
class AuditServiceTest {

    @Mock private AuditEventRepository auditEventRepository;

    @InjectMocks
    private AuditService auditService;

    // IDs como String — MongoDB usa String como tipo de id
    private final String userId = UUID.randomUUID().toString();

    private AuditEvent buildEvent(ActionType type) {
        return AuditEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .actionType(type)
                .description("Test event")
                .result(ActionResult.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should return all events when no filters are provided")
    void shouldReturnAllEventsWhenNoFilters() {
        List<AuditEvent> events = List.of(
                buildEvent(ActionType.CONSULT_SPORT_PROFILE),
                buildEvent(ActionType.UPDATE_SPORT_PROFILE)
        );
        when(auditEventRepository.findAll()).thenReturn(events);

        List<AuditEventResponse> result = auditService.getEvents(null, null, null, null);

        assertThat(result).hasSize(2);
        verify(auditEventRepository).findAll();
    }

    @Test
    @DisplayName("Should filter events by userId")
    void shouldFilterByUserId() {
        List<AuditEvent> events = List.of(buildEvent(ActionType.CONSULT_SPORT_PROFILE));
        when(auditEventRepository.findByUserId(userId)).thenReturn(events);

        List<AuditEventResponse> result = auditService.getEvents(userId, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        verify(auditEventRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Should filter events by actionType")
    void shouldFilterByActionType() {
        List<AuditEvent> events = List.of(buildEvent(ActionType.UPDATE_SPORT_PROFILE));
        when(auditEventRepository.findByActionType(ActionType.UPDATE_SPORT_PROFILE))
                .thenReturn(events);

        List<AuditEventResponse> result = auditService.getEvents(
                null, ActionType.UPDATE_SPORT_PROFILE, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActionType()).isEqualTo(ActionType.UPDATE_SPORT_PROFILE);
    }

    @Test
    @DisplayName("Should filter events by date range")
    void shouldFilterByDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to   = LocalDateTime.now();
        List<AuditEvent> events = List.of(buildEvent(ActionType.CREATE_SPORT_PROFILE));
        when(auditEventRepository.findByTimestampBetween(from, to)).thenReturn(events);

        List<AuditEventResponse> result = auditService.getEvents(null, null, from, to);

        assertThat(result).hasSize(1);
        verify(auditEventRepository).findByTimestampBetween(from, to);
    }

    @Test
    @DisplayName("Should filter events by userId and date range combined")
    void shouldFilterByUserIdAndDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to   = LocalDateTime.now();
        List<AuditEvent> events = List.of(buildEvent(ActionType.CONSULT_SPORT_PROFILE));
        when(auditEventRepository.findByUserIdAndTimestampBetween(userId, from, to))
                .thenReturn(events);

        List<AuditEventResponse> result = auditService.getEvents(userId, null, from, to);

        assertThat(result).hasSize(1);
        verify(auditEventRepository).findByUserIdAndTimestampBetween(userId, from, to);
    }

    @Test
    @DisplayName("Should return empty list when no events match the filter")
    void shouldReturnEmptyListWhenNoMatch() {
        when(auditEventRepository.findByUserId(userId)).thenReturn(List.of());

        List<AuditEventResponse> result = auditService.getEvents(userId, null, null, null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should correctly map all fields to response DTO")
    void shouldMapAllFieldsCorrectly() {
        AuditEvent event = buildEvent(ActionType.UPDATE_SPORT_PROFILE);
        when(auditEventRepository.findByUserId(userId)).thenReturn(List.of(event));

        List<AuditEventResponse> result = auditService.getEvents(userId, null, null, null);

        AuditEventResponse response = result.get(0);
        assertThat(response.getId()).isEqualTo(event.getId());
        assertThat(response.getUserId()).isEqualTo(event.getUserId());
        assertThat(response.getActionType()).isEqualTo(event.getActionType());
        assertThat(response.getDescription()).isEqualTo(event.getDescription());
        assertThat(response.getResult()).isEqualTo(event.getResult());
        assertThat(response.getTimestamp()).isEqualTo(event.getTimestamp());
    }
}