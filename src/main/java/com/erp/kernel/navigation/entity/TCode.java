package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a transaction code (T-Code) for direct navigation.
 *
 * <p>T-Codes provide shortcut-based navigation to specific application
 * screens and functions, similar to SAP transaction codes. Each code
 * maps to a route within a particular module.
 */
@Entity
@Table(name = "nav_tcode")
public class TCode extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "route", nullable = false, length = 500)
    private String route;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Returns the transaction code identifier.
     *
     * @return the T-Code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the transaction code identifier.
     *
     * @param code the T-Code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Returns the description of this transaction code.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this transaction code.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the module this transaction code belongs to.
     *
     * @return the module name
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the module this transaction code belongs to.
     *
     * @param module the module name
     */
    public void setModule(final String module) {
        this.module = module;
    }

    /**
     * Returns the route path for navigation.
     *
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * Sets the route path for navigation.
     *
     * @param route the route
     */
    public void setRoute(final String route) {
        this.route = route;
    }

    /**
     * Returns the icon identifier for UI display.
     *
     * @return the icon name, or {@code null} if not set
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon identifier for UI display.
     *
     * @param icon the icon name
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    /**
     * Returns whether the transaction code is active.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the transaction code is active.
     *
     * @param active the active flag
     */
    public void setActive(final boolean active) {
        this.active = active;
    }
}
