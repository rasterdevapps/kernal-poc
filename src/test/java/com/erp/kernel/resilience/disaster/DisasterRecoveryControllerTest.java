package com.erp.kernel.resilience.disaster;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link DisasterRecoveryController}.
 */
@WebMvcTest(DisasterRecoveryController.class)
@AutoConfigureMockMvc(addFilters = false)
class DisasterRecoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DisasterRecoveryService disasterRecoveryService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private CorsProperties corsProperties;

    private RecoveryPointResponse buildPointResponse(final Long id) {
        return new RecoveryPointResponse(id, 10L, "Snapshot " + id,
                RecoveryStatus.AVAILABLE, Instant.now(), null, "dr-site.example.com", null);
    }

    @Test
    void shouldReturnRecoveryTargets() throws Exception {
        // Arrange
        final var targets = new RecoveryTargetResponse(60, 240, "dr-site.example.com");
        when(disasterRecoveryService.getRecoveryTargets()).thenReturn(targets);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/disaster-recovery/targets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rpoMinutes").value(60))
                .andExpect(jsonPath("$.rtoMinutes").value(240))
                .andExpect(jsonPath("$.replicationTarget").value("dr-site.example.com"));
    }

    @Test
    void shouldCreateRecoveryPoint() throws Exception {
        // Arrange
        final var response = buildPointResponse(1L);
        when(disasterRecoveryService.createRecoveryPoint(
                anyLong(), anyString(), any(Instant.class), any()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/disaster-recovery/recovery-points")
                        .param("backupRecordId", "10")
                        .param("label", "Snapshot A")
                        .param("notes", "Initial snapshot"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldCreateRecoveryPoint_withoutNotes() throws Exception {
        // Arrange
        final var response = buildPointResponse(1L);
        when(disasterRecoveryService.createRecoveryPoint(
                anyLong(), anyString(), any(Instant.class), any()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/disaster-recovery/recovery-points")
                        .param("backupRecordId", "10")
                        .param("label", "Snapshot A"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnAvailableRecoveryPoints() throws Exception {
        // Arrange
        final var points = List.of(buildPointResponse(1L), buildPointResponse(2L));
        when(disasterRecoveryService.findAvailable()).thenReturn(points);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/disaster-recovery/recovery-points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnLatestRecoveryPoint_whenExists() throws Exception {
        // Arrange
        final var point = buildPointResponse(1L);
        when(disasterRecoveryService.findLatest()).thenReturn(Optional.of(point));

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/disaster-recovery/recovery-points/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404_whenNoLatestRecoveryPoint() throws Exception {
        // Arrange
        when(disasterRecoveryService.findLatest()).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/disaster-recovery/recovery-points/latest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldMarkReplicated() throws Exception {
        // Arrange
        final var response = buildPointResponse(1L);
        when(disasterRecoveryService.markReplicated(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/disaster-recovery/recovery-points/1/replicate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldMarkTested() throws Exception {
        // Arrange
        final var response = new RecoveryPointResponse(1L, 10L, "Snapshot 1",
                RecoveryStatus.TESTED, Instant.now(), null, "dr-site.example.com", null);
        when(disasterRecoveryService.markTested(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/disaster-recovery/recovery-points/1/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TESTED"));
    }
}
