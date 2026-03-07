package com.erp.kernel.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Represents the mapping between a role and a permission.
 *
 * <p>Enables many-to-many relationships between roles and permissions
 * for role-based access control (RBAC).
 */
@Entity
@Table(name = "auth_role_permission")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

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
     * Returns the permission ID.
     *
     * @return the permission ID
     */
    public Long getPermissionId() {
        return permissionId;
    }

    /**
     * Sets the permission ID.
     *
     * @param permissionId the permission ID
     */
    public void setPermissionId(final Long permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (RolePermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
