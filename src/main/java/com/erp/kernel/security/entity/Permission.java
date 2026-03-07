package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents an authorisation permission for a specific resource and action.
 *
 * <p>Permissions are assigned to roles and define what actions users with
 * those roles can perform on which resources.
 */
@Entity
@Table(name = "auth_permission")
public class Permission extends BaseEntity {

    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "resource", nullable = false, length = 200)
    private String resource;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    /**
     * Returns the permission name.
     *
     * @return the permission name
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * Sets the permission name.
     *
     * @param permissionName the permission name
     */
    public void setPermissionName(final String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the resource this permission applies to.
     *
     * @return the resource identifier
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource this permission applies to.
     *
     * @param resource the resource identifier
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }

    /**
     * Returns the action this permission allows.
     *
     * @return the action (e.g., READ, WRITE, DELETE)
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action this permission allows.
     *
     * @param action the action
     */
    public void setAction(final String action) {
        this.action = action;
    }
}
