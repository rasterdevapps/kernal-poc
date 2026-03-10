package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Records a user's recent navigation to a transaction code.
 *
 * <p>Recent navigation entries track which T-Codes a user has accessed
 * and when, enabling a "recently visited" list for quick re-navigation.
 * The {@code accessedAt} field captures the exact moment of access.
 */
@Entity
@Table(name = "nav_recent_history")
public class RecentNavigation extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tcode_id", nullable = false)
    private Long tcodeId;

    @Column(name = "accessed_at", nullable = false)
    private Instant accessedAt;

    /**
     * Returns the ID of the user who navigated.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who navigated.
     *
     * @param userId the user ID
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the ID of the accessed transaction code.
     *
     * @return the T-Code ID
     */
    public Long getTcodeId() {
        return tcodeId;
    }

    /**
     * Sets the ID of the accessed transaction code.
     *
     * @param tcodeId the T-Code ID
     */
    public void setTcodeId(final Long tcodeId) {
        this.tcodeId = tcodeId;
    }

    /**
     * Returns the timestamp when the T-Code was accessed.
     *
     * @return the access instant
     */
    public Instant getAccessedAt() {
        return accessedAt;
    }

    /**
     * Sets the timestamp when the T-Code was accessed.
     *
     * @param accessedAt the access instant
     */
    public void setAccessedAt(final Instant accessedAt) {
        this.accessedAt = accessedAt;
    }
}
