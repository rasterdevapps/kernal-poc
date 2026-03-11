package com.erp.kernel.resilience.backup;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * JPA entity representing the metadata for a single backup operation.
 *
 * <p>Records the type, status, timing, storage location, size, and checksum
 * of each backup executed by the system. Failed backups capture the error
 * message for diagnosis and remediation.
 */
@Entity
@Table(name = "backup_records")
public class BackupRecord extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "backup_type", nullable = false, length = 20)
    private BackupType backupType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BackupStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "location", length = 512)
    private String location;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "checksum", length = 128)
    private String checksum;

    @Column(name = "error_message", length = 1024)
    private String errorMessage;

    /**
     * Returns the backup type.
     *
     * @return the backup type
     */
    public BackupType getBackupType() {
        return backupType;
    }

    /**
     * Sets the backup type.
     *
     * @param backupType the backup type
     */
    public void setBackupType(final BackupType backupType) {
        this.backupType = backupType;
    }

    /**
     * Returns the current backup status.
     *
     * @return the backup status
     */
    public BackupStatus getStatus() {
        return status;
    }

    /**
     * Sets the backup status.
     *
     * @param status the backup status
     */
    public void setStatus(final BackupStatus status) {
        this.status = status;
    }

    /**
     * Returns the instant the backup was started.
     *
     * @return the start time
     */
    public Instant getStartedAt() {
        return startedAt;
    }

    /**
     * Sets the instant the backup was started.
     *
     * @param startedAt the start time
     */
    public void setStartedAt(final Instant startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * Returns the instant the backup completed, or {@code null} if still running.
     *
     * @return the completion time, or {@code null}
     */
    public Instant getCompletedAt() {
        return completedAt;
    }

    /**
     * Sets the instant the backup completed.
     *
     * @param completedAt the completion time
     */
    public void setCompletedAt(final Instant completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Returns the file system or remote storage location of the backup artefact.
     *
     * @return the storage location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the storage location of the backup artefact.
     *
     * @param location the storage location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Returns the size of the backup artefact in bytes.
     *
     * @return the size in bytes, or {@code null} if not yet known
     */
    public Long getSizeBytes() {
        return sizeBytes;
    }

    /**
     * Sets the size of the backup artefact in bytes.
     *
     * @param sizeBytes the size in bytes
     */
    public void setSizeBytes(final Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    /**
     * Returns the integrity checksum of the backup artefact.
     *
     * @return the checksum, or {@code null} if not yet computed
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Sets the integrity checksum of the backup artefact.
     *
     * @param checksum the checksum
     */
    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }

    /**
     * Returns the error message if the backup failed.
     *
     * @return the error message, or {@code null} if the backup did not fail
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message for a failed backup.
     *
     * @param errorMessage the error description
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
