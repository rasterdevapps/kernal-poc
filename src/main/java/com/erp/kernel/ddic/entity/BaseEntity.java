package com.erp.kernel.ddic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;
import java.util.Objects;

/**
 * Base class for all DDIC JPA entities providing common fields.
 *
 * <p>Provides {@code id}, {@code createdAt}, and {@code updatedAt} fields
 * with automatic timestamp management via JPA lifecycle callbacks.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Sets creation and update timestamps before the entity is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        final var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the modification timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the entity identifier.
     *
     * @return the entity ID, or {@code null} if not yet persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id the entity ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the instant when this entity was created
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation instant
     */
    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the last modification timestamp.
     *
     * @return the instant when this entity was last updated
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last modification timestamp.
     *
     * @param updatedAt the update instant
     */
    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
