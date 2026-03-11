package com.erp.kernel.resilience.disaster;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for {@link RecoveryPoint} entities.
 */
public interface RecoveryPointRepository extends JpaRepository<RecoveryPoint, Long> {

    /**
     * Returns all recovery points with the given status, ordered by capture time descending.
     *
     * @param status the status to filter by
     * @return the matching recovery points
     */
    List<RecoveryPoint> findByStatusOrderByCapturedAtDesc(RecoveryStatus status);

    /**
     * Returns the most recently captured recovery point with the given status.
     *
     * @param status the status to filter by
     * @return the most recent recovery point, or empty if none exists
     */
    Optional<RecoveryPoint> findTopByStatusOrderByCapturedAtDesc(RecoveryStatus status);
}
