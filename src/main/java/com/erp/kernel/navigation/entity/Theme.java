package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a UI theme for the presentation layer.
 *
 * <p>Themes define the visual appearance of the application including
 * primary and secondary colours. One theme may be marked as the system
 * default while individual users can override it via their preferences.
 */
@Entity
@Table(name = "nav_theme")
public class Theme extends BaseEntity {

    @Column(name = "theme_name", nullable = false, unique = true, length = 100)
    private String themeName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor;

    @Column(name = "secondary_color", nullable = false, length = 7)
    private String secondaryColor;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /**
     * Returns the theme name.
     *
     * @return the theme name
     */
    public String getThemeName() {
        return themeName;
    }

    /**
     * Sets the theme name.
     *
     * @param themeName the theme name
     */
    public void setThemeName(final String themeName) {
        this.themeName = themeName;
    }

    /**
     * Returns the theme description.
     *
     * @return the description, or {@code null} if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the theme description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the primary colour as a hex string (e.g. {@code #FF5733}).
     *
     * @return the primary colour
     */
    public String getPrimaryColor() {
        return primaryColor;
    }

    /**
     * Sets the primary colour.
     *
     * @param primaryColor the primary colour hex string
     */
    public void setPrimaryColor(final String primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * Returns the secondary colour as a hex string.
     *
     * @return the secondary colour
     */
    public String getSecondaryColor() {
        return secondaryColor;
    }

    /**
     * Sets the secondary colour.
     *
     * @param secondaryColor the secondary colour hex string
     */
    public void setSecondaryColor(final String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    /**
     * Returns whether the theme is active and available for selection.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the theme is active.
     *
     * @param active the active flag
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * Returns whether this is the system default theme.
     *
     * @return {@code true} if this is the default theme
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Sets whether this is the system default theme.
     *
     * @param isDefault the default flag
     */
    public void setDefault(final boolean isDefault) {
        this.isDefault = isDefault;
    }
}
