package com.erp.kernel.resilience.disaster;

import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for managing disaster recovery points and reporting recovery targets.
 *
 * <p>Recovery points are created from verified backup records and represent validated snapshots
 * that can be used to restore the system to a known-good state. This service tracks the
 * availability, replication, and test status of each recovery point.
 */
@Service
public class DisasterRecoveryService {

    private static final Logger LOG = LoggerFactory.getLogger(DisasterRecoveryService.class);

    private final RecoveryPointRepository recoveryPointRepository;
    private final DisasterRecoveryProperties properties;

    /**
     * Creates a new disaster recovery service.
     *
     * @param recoveryPointRepository the recovery point repository
     * @param properties              the disaster recovery configuration
     */
    public DisasterRecoveryService(final RecoveryPointRepository recoveryPointRepository,
                                   final DisasterRecoveryProperties properties) {
        this.recoveryPointRepository = Objects.requireNonNull(
                recoveryPointRepository, "recoveryPointRepository must not be null");
        this.properties = Objects.requireNonNull(
                properties, "properties must not be null");
    }

    /**
     * Returns the configured disaster recovery targets (RPO, RTO, and replication target).
     *
     * @return the recovery target configuration
     */
    public RecoveryTargetResponse getRecoveryTargets() {
        return new RecoveryTargetResponse(
                properties.rpoMinutes(),
                properties.rtoMinutes(),
                properties.replicationTarget());
    }

    /**
     * Creates a new recovery point from a verified backup record.
     *
     * @param backupRecordId the identifier of the verified backup record
     * @param label          a human-readable label for this recovery point
     * @param capturedAt     the instant the snapshot was captured
     * @param notes          optional descriptive notes
     * @return the created recovery point response
     */
    @Transactional
    public RecoveryPointResponse createRecoveryPoint(final Long backupRecordId,
                                                     final String label,
                                                     final Instant capturedAt,
                                                     final String notes) {
        Objects.requireNonNull(backupRecordId, "backupRecordId must not be null");
        Objects.requireNonNull(label, "label must not be null");
        Objects.requireNonNull(capturedAt, "capturedAt must not be null");

        final var point = new RecoveryPoint();
        point.setBackupRecordId(backupRecordId);
        point.setLabel(label);
        point.setStatus(RecoveryStatus.AVAILABLE);
        point.setCapturedAt(capturedAt);
        point.setReplicationTarget(properties.replicationTarget());
        point.setNotes(notes);

        final var saved = recoveryPointRepository.save(point);
        LOG.info("Recovery point created: id={}, label={}", saved.getId(), label);
        return toResponse(saved);
    }

    /**
     * Marks a recovery point as replicated to the off-site replication target.
     *
     * @param id the recovery point identifier
     * @return the updated recovery point response
     * @throws EntityNotFoundException if no recovery point with the given ID exists
     */
    @Transactional
    public RecoveryPointResponse markReplicated(final Long id) {
        Objects.requireNonNull(id, "id must not be null");

        final var point = findPointById(id);
        point.setReplicatedAt(Instant.now());

        final var saved = recoveryPointRepository.save(point);
        LOG.info("Recovery point replicated: id={}", id);
        return toResponse(saved);
    }

    /**
     * Marks a recovery point as successfully tested via a restore test.
     *
     * @param id the recovery point identifier
     * @return the updated recovery point response
     * @throws EntityNotFoundException if no recovery point with the given ID exists
     */
    @Transactional
    public RecoveryPointResponse markTested(final Long id) {
        Objects.requireNonNull(id, "id must not be null");

        final var point = findPointById(id);
        point.setStatus(RecoveryStatus.TESTED);

        final var saved = recoveryPointRepository.save(point);
        LOG.info("Recovery point tested: id={}", id);
        return toResponse(saved);
    }

    /**
     * Returns all available recovery points ordered by capture time descending.
     *
     * @return the list of available recovery point responses
     */
    @Transactional(readOnly = true)
    public List<RecoveryPointResponse> findAvailable() {
        return recoveryPointRepository
                .findByStatusOrderByCapturedAtDesc(RecoveryStatus.AVAILABLE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns all recovery points.
     *
     * @return the list of all recovery point responses
     */
    @Transactional(readOnly = true)
    public List<RecoveryPointResponse> findAll() {
        return recoveryPointRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns the most recently captured available recovery point.
     *
     * @return the latest recovery point, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<RecoveryPointResponse> findLatest() {
        return recoveryPointRepository
                .findTopByStatusOrderByCapturedAtDesc(RecoveryStatus.AVAILABLE)
                .map(this::toResponse);
    }

    private RecoveryPoint findPointById(final Long id) {
        return recoveryPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "RecoveryPoint with id %d not found".formatted(id)));
    }

    private RecoveryPointResponse toResponse(final RecoveryPoint point) {
        return new RecoveryPointResponse(
                point.getId(),
                point.getBackupRecordId(),
                point.getLabel(),
                point.getStatus(),
                point.getCapturedAt(),
                point.getReplicatedAt(),
                point.getReplicationTarget(),
                point.getNotes()
        );
    }
}
