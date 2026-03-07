package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a security role for role-based access control (RBAC).
 *
 * <p>Roles group permissions and are assigned to users to control access
 * to system resources, similar to SAP PFCG role management.
 */
@Entity
@Table(name = "auth_role")
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String roleName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Returns the role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the role name.
     *
     * @param roleName the role name
     */
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    /**
     * Returns the role description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the role description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns whether the role is active.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the role is active.
     *
     * @param active the active flag
     */
    public void setActive(final boolean active) {
        this.active = active;
    }
}
