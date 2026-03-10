package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a screen definition within the presentation layer.
 *
 * <p>Screens are the primary UI containers associated with a module and
 * optionally linked to a transaction code for direct navigation. Each
 * screen has a unique identifier and a type that determines its layout
 * behaviour (e.g. list, detail, dashboard).
 */
@Entity
@Table(name = "nav_screen")
public class Screen extends BaseEntity {

    @Column(name = "screen_id", nullable = false, unique = true, length = 50)
    private String screenId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "tcode_id")
    private Long tcodeId;

    @Column(name = "screen_type", nullable = false, length = 50)
    private String screenType;

    /**
     * Returns the unique screen identifier.
     *
     * @return the screen ID
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Sets the unique screen identifier.
     *
     * @param screenId the screen ID
     */
    public void setScreenId(final String screenId) {
        this.screenId = screenId;
    }

    /**
     * Returns the screen title displayed in the UI.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the screen title displayed in the UI.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the screen description.
     *
     * @return the description, or {@code null} if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the screen description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the module this screen belongs to.
     *
     * @return the module name
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the module this screen belongs to.
     *
     * @param module the module name
     */
    public void setModule(final String module) {
        this.module = module;
    }

    /**
     * Returns the associated transaction code identifier.
     *
     * @return the T-Code ID, or {@code null} if not linked
     */
    public Long getTcodeId() {
        return tcodeId;
    }

    /**
     * Sets the associated transaction code identifier.
     *
     * @param tcodeId the T-Code ID
     */
    public void setTcodeId(final Long tcodeId) {
        this.tcodeId = tcodeId;
    }

    /**
     * Returns the screen type (e.g. list, detail, dashboard).
     *
     * @return the screen type
     */
    public String getScreenType() {
        return screenType;
    }

    /**
     * Sets the screen type.
     *
     * @param screenType the screen type
     */
    public void setScreenType(final String screenType) {
        this.screenType = screenType;
    }
}
