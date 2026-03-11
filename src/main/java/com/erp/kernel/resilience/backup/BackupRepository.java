package com.erp.kernel.resilience.backup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for {@link BackupRecord} entities.
 */
public interface BackupRepository extends JpaRepository<BackupRecord, Long> {

    /**
     * Retrieves all backup records with the given status, ordered by start time descending.
     *
     * @param status the status to filter by
     * @return the list of matching backup records
     */
    List<BackupRecord> findByStatusOrderByStartedAtDesc(BackupStatus status);
}
