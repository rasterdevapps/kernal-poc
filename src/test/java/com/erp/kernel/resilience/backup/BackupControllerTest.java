package com.erp.kernel.resilience.backup;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link BackupController}.
 */
@WebMvcTest(BackupController.class)
@AutoConfigureMockMvc(addFilters = false)
class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BackupService backupService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private CorsProperties corsProperties;

    private BackupResponse buildResponse(final Long id, final BackupType type,
                                          final BackupStatus status) {
        return new BackupResponse(id, type, status, Instant.now(), null,
                "/var/erp/backups/" + id + ".bak", null, null, null);
    }

    @Test
    void shouldInitiateBackup_whenValidRequest() throws Exception {
        // Arrange
        final var response = buildResponse(1L, BackupType.FULL, BackupStatus.IN_PROGRESS);
        when(backupService.initiateBackup(BackupType.FULL)).thenReturn(response);

        final var request = new BackupRequest(BackupType.FULL);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/backups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturnAllBackups() throws Exception {
        // Arrange
        final var responses = List.of(
                buildResponse(1L, BackupType.FULL, BackupStatus.COMPLETED),
                buildResponse(2L, BackupType.DIFFERENTIAL, BackupStatus.FAILED));
        when(backupService.findAll()).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/backups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnBackupById() throws Exception {
        // Arrange
        final var response = buildResponse(1L, BackupType.FULL, BackupStatus.COMPLETED);
        when(backupService.findById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/backups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnBackupsByStatus() throws Exception {
        // Arrange
        final var responses = List.of(buildResponse(1L, BackupType.FULL, BackupStatus.VERIFIED));
        when(backupService.findByStatus(BackupStatus.VERIFIED)).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resilience/backups/by-status")
                        .param("status", "VERIFIED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldCompleteBackup() throws Exception {
        // Arrange
        final var response = buildResponse(1L, BackupType.FULL, BackupStatus.COMPLETED);
        when(backupService.completeBackup(1L, 2048L, "sha256:abc")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/backups/1/complete")
                        .param("sizeBytes", "2048")
                        .param("checksum", "sha256:abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldFailBackup() throws Exception {
        // Arrange
        final var response = buildResponse(1L, BackupType.FULL, BackupStatus.FAILED);
        when(backupService.failBackup(1L, "disk full")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/backups/1/fail")
                        .param("errorMessage", "disk full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void shouldVerifyBackup() throws Exception {
        // Arrange
        final var response = buildResponse(1L, BackupType.FULL, BackupStatus.VERIFIED);
        when(backupService.verifyBackup(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resilience/backups/1/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VERIFIED"));
    }

    @Test
    void shouldThrowExceptionWhenBackupRequestBodyIsInvalid() throws Exception {
        // Act & Assert - null backupType should fail validation
        mockMvc.perform(post("/api/v1/resilience/backups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"backupType\": null}"))
                .andExpect(status().isBadRequest());
    }
}
