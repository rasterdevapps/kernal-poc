package com.erp.kernel.resilience.backup;

import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service responsible for managing backup operations.
 *
 * <p>Provides methods to initiate, complete, fail, verify, and query database backups.
 * In a production deployment, this service would invoke database-native backup utilities
 * (e.g., pg_dump for PostgreSQL); in this PoC implementation it records backup metadata
 * and simulates the execution lifecycle.
 */
@Service
public class BackupService {

    private static final Logger LOG = LoggerFactory.getLogger(BackupService.class);

    private final BackupRepository backupRepository;
    private final BackupProperties backupProperties;

    /**
     * Creates a new backup service.
     *
     * @param backupRepository the backup record repository
     * @param backupProperties the backup configuration properties
     */
    public BackupService(final BackupRepository backupRepository,
                         final BackupProperties backupProperties) {
        this.backupRepository = Objects.requireNonNull(
                backupRepository, "backupRepository must not be null");
        this.backupProperties = Objects.requireNonNull(
                backupProperties, "backupProperties must not be null");
    }

    /**
     * Initiates a new backup operation of the specified type.
     *
     * @param backupType the type of backup to perform
     * @return the created backup record
     * @throws IllegalStateException if the backup system is disabled
     */
    @Transactional
    public BackupResponse initiateBackup(final BackupType backupType) {
        Objects.requireNonNull(backupType, "backupType must not be null");

        if (!backupProperties.enabled()) {
            throw new IllegalStateException("Backup system is disabled");
        }

        final var record = new BackupRecord();
        record.setBackupType(backupType);
        record.setStatus(BackupStatus.IN_PROGRESS);
        record.setStartedAt(Instant.now());
        record.setLocation(backupProperties.storagePath()
                + "/" + UUID.randomUUID()
                + "-" + backupType.name().toLowerCase() + ".bak");

        final var saved = backupRepository.save(record);
        LOG.info("Backup initiated: id={}, type={}, location={}",
                saved.getId(), backupType, saved.getLocation());
        return toResponse(saved);
    }

    /**
     * Marks a backup as completed with integrity metadata.
     *
     * @param id        the backup record identifier
     * @param sizeBytes the size of the backup artefact in bytes
     * @param checksum  the integrity checksum of the backup artefact
     * @return the updated backup record
     * @throws EntityNotFoundException if no record with the given ID exists
     */
    @Transactional
    public BackupResponse completeBackup(final Long id, final Long sizeBytes,
                                         final String checksum) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(sizeBytes, "sizeBytes must not be null");
        Objects.requireNonNull(checksum, "checksum must not be null");

        final var record = findRecordById(id);
        record.setStatus(BackupStatus.COMPLETED);
        record.setCompletedAt(Instant.now());
        record.setSizeBytes(sizeBytes);
        record.setChecksum(checksum);

        final var saved = backupRepository.save(record);
        LOG.info("Backup completed: id={}, size={} bytes", id, sizeBytes);
        return toResponse(saved);
    }

    /**
     * Marks a backup as failed with an error description.
     *
     * @param id           the backup record identifier
     * @param errorMessage the error description
     * @return the updated backup record
     * @throws EntityNotFoundException if no record with the given ID exists
     */
    @Transactional
    public BackupResponse failBackup(final Long id, final String errorMessage) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(errorMessage, "errorMessage must not be null");

        final var record = findRecordById(id);
        record.setStatus(BackupStatus.FAILED);
        record.setCompletedAt(Instant.now());
        record.setErrorMessage(errorMessage);

        final var saved = backupRepository.save(record);
        LOG.error("Backup failed: id={}, error={}", id, errorMessage);
        return toResponse(saved);
    }

    /**
     * Marks a completed backup as integrity-verified and eligible for disaster recovery.
     *
     * @param id the backup record identifier
     * @return the updated backup record
     * @throws EntityNotFoundException if no record with the given ID exists
     * @throws IllegalStateException   if the backup is not in COMPLETED status
     */
    @Transactional
    public BackupResponse verifyBackup(final Long id) {
        Objects.requireNonNull(id, "id must not be null");

        final var record = findRecordById(id);
        if (record.getStatus() != BackupStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Only COMPLETED backups can be verified; current status: "
                    + record.getStatus());
        }

        record.setStatus(BackupStatus.VERIFIED);
        final var saved = backupRepository.save(record);
        LOG.info("Backup verified: id={}", id);
        return toResponse(saved);
    }

    /**
     * Returns all backup records.
     *
     * @return the list of all backup responses
     */
    @Transactional(readOnly = true)
    public List<BackupResponse> findAll() {
        return backupRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns a backup record by its identifier.
     *
     * @param id the backup record ID
     * @return the backup response
     * @throws EntityNotFoundException if no record with the given ID exists
     */
    @Transactional(readOnly = true)
    public BackupResponse findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return toResponse(findRecordById(id));
    }

    /**
     * Returns all backup records with the given status.
     *
     * @param status the status to filter by
     * @return the matching backup responses
     */
    @Transactional(readOnly = true)
    public List<BackupResponse> findByStatus(final BackupStatus status) {
        Objects.requireNonNull(status, "status must not be null");
        return backupRepository.findByStatusOrderByStartedAtDesc(status).stream()
                .map(this::toResponse)
                .toList();
    }

    private BackupRecord findRecordById(final Long id) {
        return backupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "BackupRecord with id %d not found".formatted(id)));
    }

    private BackupResponse toResponse(final BackupRecord record) {
        return new BackupResponse(
                record.getId(),
                record.getBackupType(),
                record.getStatus(),
                record.getStartedAt(),
                record.getCompletedAt(),
                record.getLocation(),
                record.getSizeBytes(),
                record.getChecksum(),
                record.getErrorMessage()
        );
    }
}
