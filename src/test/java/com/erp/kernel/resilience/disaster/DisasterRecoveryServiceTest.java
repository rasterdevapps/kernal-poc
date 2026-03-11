package com.erp.kernel.resilience.disaster;

import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DisasterRecoveryService}.
 */
@ExtendWith(MockitoExtension.class)
class DisasterRecoveryServiceTest {

    @Mock
    private RecoveryPointRepository recoveryPointRepository;

    private DisasterRecoveryService service;

    @BeforeEach
    void setUp() {
        final var properties = new DisasterRecoveryProperties(60, 240, "dr-site.example.com");
        service = new DisasterRecoveryService(recoveryPointRepository, properties);
    }

    @Test
    void shouldThrowNullPointerException_whenRepositoryIsNull() {
        final var properties = new DisasterRecoveryProperties(60, 240, "");
        assertThatThrownBy(() -> new DisasterRecoveryService(null, properties))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("recoveryPointRepository must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new DisasterRecoveryService(recoveryPointRepository, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldReturnRecoveryTargets() {
        // Act
        final var targets = service.getRecoveryTargets();

        // Assert
        assertThat(targets.rpoMinutes()).isEqualTo(60);
        assertThat(targets.rtoMinutes()).isEqualTo(240);
        assertThat(targets.replicationTarget()).isEqualTo("dr-site.example.com");
    }

    @Test
    void shouldCreateRecoveryPoint_whenValidInputs() {
        // Arrange
        final var now = Instant.now();
        final var savedPoint = buildPoint(1L, RecoveryStatus.AVAILABLE, now);
        when(recoveryPointRepository.save(any())).thenReturn(savedPoint);

        // Act
        final var response = service.createRecoveryPoint(10L, "Snapshot A", now, "Initial");

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo(RecoveryStatus.AVAILABLE);
        verify(recoveryPointRepository).save(any());
    }

    @Test
    void shouldThrowNullPointerException_whenCreateRecoveryPointBackupRecordIdIsNull() {
        assertThatThrownBy(() -> service.createRecoveryPoint(null, "label", Instant.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("backupRecordId must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenCreateRecoveryPointLabelIsNull() {
        assertThatThrownBy(() -> service.createRecoveryPoint(1L, null, Instant.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("label must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenCreateRecoveryPointCapturedAtIsNull() {
        assertThatThrownBy(() -> service.createRecoveryPoint(1L, "label", null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("capturedAt must not be null");
    }

    @Test
    void shouldMarkReplicated_whenValidId() {
        // Arrange
        final var now = Instant.now();
        final var point = buildPoint(1L, RecoveryStatus.AVAILABLE, now);
        when(recoveryPointRepository.findById(1L)).thenReturn(Optional.of(point));
        when(recoveryPointRepository.save(any())).thenReturn(point);

        // Act
        final var response = service.markReplicated(1L);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        verify(recoveryPointRepository).save(any());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenMarkReplicatedIdNotFound() {
        when(recoveryPointRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.markReplicated(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("RecoveryPoint with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenMarkReplicatedIdIsNull() {
        assertThatThrownBy(() -> service.markReplicated(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldMarkTested_whenValidId() {
        // Arrange
        final var now = Instant.now();
        final var point = buildPoint(1L, RecoveryStatus.AVAILABLE, now);
        final var testedPoint = buildPoint(1L, RecoveryStatus.TESTED, now);
        when(recoveryPointRepository.findById(1L)).thenReturn(Optional.of(point));
        when(recoveryPointRepository.save(any())).thenReturn(testedPoint);

        // Act
        final var response = service.markTested(1L);

        // Assert
        assertThat(response.status()).isEqualTo(RecoveryStatus.TESTED);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenMarkTestedIdNotFound() {
        when(recoveryPointRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.markTested(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("RecoveryPoint with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenMarkTestedIdIsNull() {
        assertThatThrownBy(() -> service.markTested(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldReturnAvailableRecoveryPoints() {
        // Arrange
        final var now = Instant.now();
        final var points = List.of(buildPoint(1L, RecoveryStatus.AVAILABLE, now));
        when(recoveryPointRepository.findByStatusOrderByCapturedAtDesc(RecoveryStatus.AVAILABLE))
                .thenReturn(points);

        // Act
        final var result = service.findAvailable();

        // Assert
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnAllRecoveryPoints() {
        // Arrange
        final var now = Instant.now();
        final var points = List.of(
                buildPoint(1L, RecoveryStatus.AVAILABLE, now),
                buildPoint(2L, RecoveryStatus.TESTED, now.minusSeconds(3600)));
        when(recoveryPointRepository.findAll()).thenReturn(points);

        // Act
        final var result = service.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnLatestAvailableRecoveryPoint_whenExists() {
        // Arrange
        final var now = Instant.now();
        final var point = buildPoint(1L, RecoveryStatus.AVAILABLE, now);
        when(recoveryPointRepository.findTopByStatusOrderByCapturedAtDesc(RecoveryStatus.AVAILABLE))
                .thenReturn(Optional.of(point));

        // Act
        final var result = service.findLatest();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmpty_whenNoLatestAvailableRecoveryPoint() {
        // Arrange
        when(recoveryPointRepository.findTopByStatusOrderByCapturedAtDesc(RecoveryStatus.AVAILABLE))
                .thenReturn(Optional.empty());

        // Act
        final var result = service.findLatest();

        // Assert
        assertThat(result).isEmpty();
    }

    private RecoveryPoint buildPoint(final Long id, final RecoveryStatus status,
                                     final Instant capturedAt) {
        final var point = new RecoveryPoint();
        point.setId(id);
        point.setBackupRecordId(10L);
        point.setLabel("Test snapshot " + id);
        point.setStatus(status);
        point.setCapturedAt(capturedAt);
        point.setReplicationTarget("dr-site.example.com");
        return point;
    }
}
