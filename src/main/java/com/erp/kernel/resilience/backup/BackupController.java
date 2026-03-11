package com.erp.kernel.resilience.backup;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing backup operations.
 *
 * <p>Provides endpoints for triggering, tracking, completing, and verifying backups
 * at {@code /api/v1/resilience/backups}.
 */
@RestController
@RequestMapping("/api/v1/resilience/backups")
public class BackupController {

    private final BackupService backupService;

    /**
     * Creates a new backup controller.
     *
     * @param backupService the backup service
     */
    public BackupController(final BackupService backupService) {
        this.backupService = Objects.requireNonNull(
                backupService, "backupService must not be null");
    }

    /**
     * Triggers a new backup operation.
     *
     * @param request the backup request specifying the type
     * @return the created backup record with HTTP 202 Accepted
     */
    @PostMapping
    public ResponseEntity<BackupResponse> initiateBackup(
            @Valid @RequestBody final BackupRequest request) {
        final var response = backupService.initiateBackup(request.backupType());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * Returns all backup records.
     *
     * @return the list of all backup records
     */
    @GetMapping
    public ResponseEntity<List<BackupResponse>> findAll() {
        return ResponseEntity.ok(backupService.findAll());
    }

    /**
     * Returns a backup record by its identifier.
     *
     * @param id the backup record ID
     * @return the backup record
     */
    @GetMapping("/{id}")
    public ResponseEntity<BackupResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(backupService.findById(id));
    }

    /**
     * Returns backup records filtered by status.
     *
     * @param status the status filter
     * @return the filtered backup records
     */
    @GetMapping("/by-status")
    public ResponseEntity<List<BackupResponse>> findByStatus(
            @RequestParam final BackupStatus status) {
        return ResponseEntity.ok(backupService.findByStatus(status));
    }

    /**
     * Marks a backup as successfully completed with size and checksum metadata.
     *
     * @param id        the backup record ID
     * @param sizeBytes the size of the backup artefact in bytes
     * @param checksum  the integrity checksum
     * @return the updated backup record
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<BackupResponse> completeBackup(
            @PathVariable final Long id,
            @RequestParam final Long sizeBytes,
            @RequestParam final String checksum) {
        return ResponseEntity.ok(backupService.completeBackup(id, sizeBytes, checksum));
    }

    /**
     * Marks a backup as failed with an error description.
     *
     * @param id           the backup record ID
     * @param errorMessage the error description
     * @return the updated backup record
     */
    @PostMapping("/{id}/fail")
    public ResponseEntity<BackupResponse> failBackup(
            @PathVariable final Long id,
            @RequestParam final String errorMessage) {
        return ResponseEntity.ok(backupService.failBackup(id, errorMessage));
    }

    /**
     * Marks a completed backup as integrity-verified.
     *
     * @param id the backup record ID
     * @return the updated backup record
     */
    @PostMapping("/{id}/verify")
    public ResponseEntity<BackupResponse> verifyBackup(@PathVariable final Long id) {
        return ResponseEntity.ok(backupService.verifyBackup(id));
    }
}
