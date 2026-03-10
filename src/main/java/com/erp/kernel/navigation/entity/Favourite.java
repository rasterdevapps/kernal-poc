package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents a user's favourite transaction code for quick access.
 *
 * <p>Favourites allow users to bookmark frequently used T-Codes and
 * arrange them in a custom sort order for personalised navigation.
 * Each user may favourite a given T-Code at most once.
 */
@Entity
@Table(name = "nav_favourite", uniqueConstraints =
        @UniqueConstraint(columnNames = {"user_id", "tcode_id"}))
public class Favourite extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tcode_id", nullable = false)
    private Long tcodeId;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    /**
     * Returns the ID of the user who owns this favourite.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who owns this favourite.
     *
     * @param userId the user ID
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the ID of the favourited transaction code.
     *
     * @return the T-Code ID
     */
    public Long getTcodeId() {
        return tcodeId;
    }

    /**
     * Sets the ID of the favourited transaction code.
     *
     * @param tcodeId the T-Code ID
     */
    public void setTcodeId(final Long tcodeId) {
        this.tcodeId = tcodeId;
    }

    /**
     * Returns the display sort order.
     *
     * @return the sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the display sort order.
     *
     * @param sortOrder the sort order
     */
    public void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
