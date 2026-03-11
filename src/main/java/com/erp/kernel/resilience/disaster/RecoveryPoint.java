package com.erp.kernel.resilience.disaster;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * JPA entity representing a disaster recovery point.
 *
 * <p>A recovery point is a verified snapshot of the database that can be used to restore
 * the system to a known-good state. Recovery points are created from verified backup records
 * and track RPO compliance and off-site replication state.
 */
@Entity
@Table(name = "recovery_points")
public class RecoveryPoint extends BaseEntity {

    @Column(name = "backup_record_id", nullable = false)
    private Long backupRecordId;

    @Column(name = "label", nullable = false, length = 255)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecoveryStatus status;

    @Column(name = "captured_at", nullable = false)
    private Instant capturedAt;

    @Column(name = "replicated_at")
    private Instant replicatedAt;

    @Column(name = "replication_target", length = 512)
    private String replicationTarget;

    @Column(name = "notes", length = 1024)
    private String notes;

    /**
     * Returns the identifier of the underlying backup record.
     *
     * @return the backup record ID
     */
    public Long getBackupRecordId() {
        return backupRecordId;
    }

    /**
     * Sets the identifier of the underlying backup record.
     *
     * @param backupRecordId the backup record ID
     */
    public void setBackupRecordId(final Long backupRecordId) {
        this.backupRecordId = backupRecordId;
    }

    /**
     * Returns the human-readable label for this recovery point.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the human-readable label.
     *
     * @param label the label
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Returns the current status of this recovery point.
     *
     * @return the status
     */
    public RecoveryStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this recovery point.
     *
     * @param status the status
     */
    public void setStatus(final RecoveryStatus status) {
        this.status = status;
    }

    /**
     * Returns the instant this recovery point snapshot was captured.
     *
     * @return the capture time
     */
    public Instant getCapturedAt() {
        return capturedAt;
    }

    /**
     * Sets the capture time.
     *
     * @param capturedAt the capture time
     */
    public void setCapturedAt(final Instant capturedAt) {
        this.capturedAt = capturedAt;
    }

    /**
     * Returns the instant this recovery point was replicated off-site,
     * or {@code null} if not yet replicated.
     *
     * @return the replication time, or {@code null}
     */
    public Instant getReplicatedAt() {
        return replicatedAt;
    }

    /**
     * Sets the replication time.
     *
     * @param replicatedAt the replication time
     */
    public void setReplicatedAt(final Instant replicatedAt) {
        this.replicatedAt = replicatedAt;
    }

    /**
     * Returns the host or URL of the off-site replication target.
     *
     * @return the replication target
     */
    public String getReplicationTarget() {
        return replicationTarget;
    }

    /**
     * Sets the replication target.
     *
     * @param replicationTarget the replication target
     */
    public void setReplicationTarget(final String replicationTarget) {
        this.replicationTarget = replicationTarget;
    }

    /**
     * Returns optional descriptive notes for this recovery point.
     *
     * @return the notes, or {@code null}
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets optional descriptive notes.
     *
     * @param notes the notes
     */
    public void setNotes(final String notes) {
        this.notes = notes;
    }
}
