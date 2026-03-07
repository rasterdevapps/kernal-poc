package com.erp.kernel.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents the mapping between a user and a role.
 *
 * <p>Enables many-to-many relationships between users and roles
 * for role-based access control (RBAC).
 */
@Entity
@Table(name = "auth_user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    /**
     * Returns the entity identifier.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id the ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the role ID.
     *
     * @return the role ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID.
     *
     * @param roleId the role ID
     */
    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns the assignment timestamp.
     *
     * @return the instant when the role was assigned
     */
    public Instant getAssignedAt() {
        return assignedAt;
    }

    /**
     * Sets the assignment timestamp.
     *
     * @param assignedAt the assignment instant
     */
    public void setAssignedAt(final Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (UserRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
