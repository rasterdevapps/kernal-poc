package com.erp.kernel.resilience.backup;

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
 * Tests for {@link BackupService}.
 */
@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

    @Mock
    private BackupRepository backupRepository;

    private BackupService backupService;
    private BackupService disabledBackupService;

    @BeforeEach
    void setUp() {
        final var enabledProperties = new BackupProperties(true, 30, "/var/erp/backups");
        final var disabledProperties = new BackupProperties(false, 30, "/var/erp/backups");
        backupService = new BackupService(backupRepository, enabledProperties);
        disabledBackupService = new BackupService(backupRepository, disabledProperties);
    }

    @Test
    void shouldThrowNullPointerException_whenBackupRepositoryIsNull() {
        final var properties = new BackupProperties(true, 30, "/tmp");
        assertThatThrownBy(() -> new BackupService(null, properties))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("backupRepository must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenBackupPropertiesIsNull() {
        assertThatThrownBy(() -> new BackupService(backupRepository, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("backupProperties must not be null");
    }

    @Test
    void shouldInitiateBackup_whenEnabledAndValidType() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.IN_PROGRESS);
        when(backupRepository.save(any())).thenReturn(record);

        // Act
        final var response = backupService.initiateBackup(BackupType.FULL);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.backupType()).isEqualTo(BackupType.FULL);
        assertThat(response.status()).isEqualTo(BackupStatus.IN_PROGRESS);
        verify(backupRepository).save(any());
    }

    @Test
    void shouldThrowIllegalStateException_whenBackupSystemDisabled() {
        assertThatThrownBy(() -> disabledBackupService.initiateBackup(BackupType.FULL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Backup system is disabled");
    }

    @Test
    void shouldThrowNullPointerException_whenInitiateBackupTypeIsNull() {
        assertThatThrownBy(() -> backupService.initiateBackup(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("backupType must not be null");
    }

    @Test
    void shouldCompleteBackup_whenValidIdAndMetadata() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.COMPLETED);
        record.setSizeBytes(1024L);
        record.setChecksum("sha256:abc");
        record.setCompletedAt(Instant.now());
        when(backupRepository.findById(1L)).thenReturn(Optional.of(record));
        when(backupRepository.save(any())).thenReturn(record);

        // Act
        final var response = backupService.completeBackup(1L, 1024L, "sha256:abc");

        // Assert
        assertThat(response.status()).isEqualTo(BackupStatus.COMPLETED);
        assertThat(response.sizeBytes()).isEqualTo(1024L);
        assertThat(response.checksum()).isEqualTo("sha256:abc");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenCompleteBackupIdNotFound() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> backupService.completeBackup(99L, 100L, "chk"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BackupRecord with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenCompleteBackupIdIsNull() {
        assertThatThrownBy(() -> backupService.completeBackup(null, 100L, "chk"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenCompleteBackupSizeBytesIsNull() {
        assertThatThrownBy(() -> backupService.completeBackup(1L, null, "chk"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sizeBytes must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenCompleteBackupChecksumIsNull() {
        assertThatThrownBy(() -> backupService.completeBackup(1L, 100L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("checksum must not be null");
    }

    @Test
    void shouldFailBackup_whenValidIdAndError() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.FAILED);
        record.setErrorMessage("disk full");
        record.setCompletedAt(Instant.now());
        when(backupRepository.findById(1L)).thenReturn(Optional.of(record));
        when(backupRepository.save(any())).thenReturn(record);

        // Act
        final var response = backupService.failBackup(1L, "disk full");

        // Assert
        assertThat(response.status()).isEqualTo(BackupStatus.FAILED);
        assertThat(response.errorMessage()).isEqualTo("disk full");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenFailBackupIdNotFound() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> backupService.failBackup(99L, "error"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BackupRecord with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenFailBackupIdIsNull() {
        assertThatThrownBy(() -> backupService.failBackup(null, "error"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenFailBackupErrorMessageIsNull() {
        assertThatThrownBy(() -> backupService.failBackup(1L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorMessage must not be null");
    }

    @Test
    void shouldVerifyBackup_whenStatusIsCompleted() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.COMPLETED);
        final var verifiedRecord = buildRecord(1L, BackupType.FULL, BackupStatus.VERIFIED);
        when(backupRepository.findById(1L)).thenReturn(Optional.of(record));
        when(backupRepository.save(any())).thenReturn(verifiedRecord);

        // Act
        final var response = backupService.verifyBackup(1L);

        // Assert
        assertThat(response.status()).isEqualTo(BackupStatus.VERIFIED);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyBackupStatusIsNotCompleted() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.IN_PROGRESS);
        when(backupRepository.findById(1L)).thenReturn(Optional.of(record));

        // Act & Assert
        assertThatThrownBy(() -> backupService.verifyBackup(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only COMPLETED backups can be verified");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenVerifyBackupIdNotFound() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> backupService.verifyBackup(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BackupRecord with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenVerifyBackupIdIsNull() {
        assertThatThrownBy(() -> backupService.verifyBackup(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldReturnAllBackupRecords() {
        // Arrange
        final var records = List.of(
                buildRecord(1L, BackupType.FULL, BackupStatus.COMPLETED),
                buildRecord(2L, BackupType.DIFFERENTIAL, BackupStatus.FAILED));
        when(backupRepository.findAll()).thenReturn(records);

        // Act
        final var result = backupService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(1).id()).isEqualTo(2L);
    }

    @Test
    void shouldReturnBackupById_whenExists() {
        // Arrange
        final var record = buildRecord(1L, BackupType.FULL, BackupStatus.COMPLETED);
        when(backupRepository.findById(1L)).thenReturn(Optional.of(record));

        // Act
        final var response = backupService.findById(1L);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenFindByIdNotFound() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> backupService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BackupRecord with id 99 not found");
    }

    @Test
    void shouldThrowNullPointerException_whenFindByIdIsNull() {
        assertThatThrownBy(() -> backupService.findById(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldReturnBackupsByStatus() {
        // Arrange
        final var records = List.of(
                buildRecord(1L, BackupType.FULL, BackupStatus.COMPLETED));
        when(backupRepository.findByStatusOrderByStartedAtDesc(BackupStatus.COMPLETED))
                .thenReturn(records);

        // Act
        final var result = backupService.findByStatus(BackupStatus.COMPLETED);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(BackupStatus.COMPLETED);
    }

    @Test
    void shouldThrowNullPointerException_whenFindByStatusIsNull() {
        assertThatThrownBy(() -> backupService.findByStatus(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("status must not be null");
    }

    private BackupRecord buildRecord(final Long id, final BackupType type,
                                     final BackupStatus status) {
        final var record = new BackupRecord();
        record.setId(id);
        record.setBackupType(type);
        record.setStatus(status);
        record.setStartedAt(Instant.now());
        record.setLocation("/var/erp/backups/" + id + ".bak");
        return record;
    }
}
