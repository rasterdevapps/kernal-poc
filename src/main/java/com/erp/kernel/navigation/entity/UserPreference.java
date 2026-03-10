package com.erp.kernel.navigation.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stores per-user presentation preferences and personalisation settings.
 *
 * <p>Each user has at most one preference record controlling their theme,
 * locale, date/time format, and pagination defaults. The referenced theme
 * is optional; when absent the system default theme is applied.
 */
@Entity
@Table(name = "nav_user_preference")
public class UserPreference extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "locale", nullable = false, length = 10)
    private String locale = "en";

    @Column(name = "date_format", nullable = false, length = 20)
    private String dateFormat = "yyyy-MM-dd";

    @Column(name = "time_format", nullable = false, length = 20)
    private String timeFormat = "HH:mm:ss";

    @Column(name = "items_per_page", nullable = false)
    private int itemsPerPage = 20;

    /**
     * Returns the ID of the user this preference belongs to.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user this preference belongs to.
     *
     * @param userId the user ID
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the ID of the selected theme.
     *
     * @return the theme ID, or {@code null} if using the system default
     */
    public Long getThemeId() {
        return themeId;
    }

    /**
     * Sets the ID of the selected theme.
     *
     * @param themeId the theme ID
     */
    public void setThemeId(final Long themeId) {
        this.themeId = themeId;
    }

    /**
     * Returns the preferred locale (e.g. {@code en}, {@code de}).
     *
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the preferred locale.
     *
     * @param locale the locale
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    /**
     * Returns the preferred date format pattern (e.g. {@code yyyy-MM-dd}).
     *
     * @return the date format
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the preferred date format pattern.
     *
     * @param dateFormat the date format
     */
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Returns the preferred time format pattern (e.g. {@code HH:mm:ss}).
     *
     * @return the time format
     */
    public String getTimeFormat() {
        return timeFormat;
    }

    /**
     * Sets the preferred time format pattern.
     *
     * @param timeFormat the time format
     */
    public void setTimeFormat(final String timeFormat) {
        this.timeFormat = timeFormat;
    }

    /**
     * Returns the number of items displayed per page in list views.
     *
     * @return the items per page
     */
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * Sets the number of items displayed per page in list views.
     *
     * @param itemsPerPage the items per page
     */
    public void setItemsPerPage(final int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
