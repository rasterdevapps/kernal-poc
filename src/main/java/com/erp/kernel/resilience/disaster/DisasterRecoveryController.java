package com.erp.kernel.resilience.disaster;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for disaster recovery management.
 *
 * <p>Provides endpoints to query recovery targets and manage recovery points
 * at {@code /api/v1/resilience/disaster-recovery}.
 */
@RestController
@RequestMapping("/api/v1/resilience/disaster-recovery")
public class DisasterRecoveryController {

    private final DisasterRecoveryService disasterRecoveryService;

    /**
     * Creates a new disaster recovery controller.
     *
     * @param disasterRecoveryService the disaster recovery service
     */
    public DisasterRecoveryController(final DisasterRecoveryService disasterRecoveryService) {
        this.disasterRecoveryService = Objects.requireNonNull(
                disasterRecoveryService, "disasterRecoveryService must not be null");
    }

    /**
     * Returns the configured recovery targets (RPO, RTO, and replication target).
     *
     * @return the recovery target configuration
     */
    @GetMapping("/targets")
    public ResponseEntity<RecoveryTargetResponse> getRecoveryTargets() {
        return ResponseEntity.ok(disasterRecoveryService.getRecoveryTargets());
    }

    /**
     * Creates a new recovery point from a verified backup record.
     *
     * @param backupRecordId the verified backup record ID
     * @param label          a human-readable label
     * @param notes          optional descriptive notes
     * @return the created recovery point with HTTP 201
     */
    @PostMapping("/recovery-points")
    public ResponseEntity<RecoveryPointResponse> createRecoveryPoint(
            @RequestParam final Long backupRecordId,
            @RequestParam final String label,
            @RequestParam(required = false) final String notes) {
        final var response = disasterRecoveryService.createRecoveryPoint(
                backupRecordId, label, Instant.now(), notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Returns all available recovery points.
     *
     * @return the list of available recovery points
     */
    @GetMapping("/recovery-points")
    public ResponseEntity<List<RecoveryPointResponse>> findAvailable() {
        return ResponseEntity.ok(disasterRecoveryService.findAvailable());
    }

    /**
     * Returns the most recently captured available recovery point.
     *
     * @return the latest recovery point, or HTTP 404 if none exists
     */
    @GetMapping("/recovery-points/latest")
    public ResponseEntity<RecoveryPointResponse> findLatest() {
        return disasterRecoveryService.findLatest()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Marks a recovery point as replicated to the off-site target.
     *
     * @param id the recovery point ID
     * @return the updated recovery point
     */
    @PostMapping("/recovery-points/{id}/replicate")
    public ResponseEntity<RecoveryPointResponse> markReplicated(@PathVariable final Long id) {
        return ResponseEntity.ok(disasterRecoveryService.markReplicated(id));
    }

    /**
     * Marks a recovery point as successfully tested via a restore test.
     *
     * @param id the recovery point ID
     * @return the updated recovery point
     */
    @PostMapping("/recovery-points/{id}/test")
    public ResponseEntity<RecoveryPointResponse> markTested(@PathVariable final Long id) {
        return ResponseEntity.ok(disasterRecoveryService.markTested(id));
    }
}
