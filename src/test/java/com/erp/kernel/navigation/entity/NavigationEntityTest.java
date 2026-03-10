package com.erp.kernel.navigation.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for navigation entity classes.
 */
class NavigationEntityTest {

    // ---- Theme ----

    @Test
    void shouldSetAndGetThemeFields() {
        final var theme = new Theme();
        theme.setThemeName("Dark Mode");
        theme.setDescription("A dark theme");
        theme.setPrimaryColor("#000000");
        theme.setSecondaryColor("#FFFFFF");
        theme.setActive(true);
        theme.setDefault(true);

        assertThat(theme.getThemeName()).isEqualTo("Dark Mode");
        assertThat(theme.getDescription()).isEqualTo("A dark theme");
        assertThat(theme.getPrimaryColor()).isEqualTo("#000000");
        assertThat(theme.getSecondaryColor()).isEqualTo("#FFFFFF");
        assertThat(theme.isActive()).isTrue();
        assertThat(theme.isDefault()).isTrue();
    }

    @Test
    void shouldDefaultThemeActiveToTrue() {
        final var theme = new Theme();
        assertThat(theme.isActive()).isTrue();
    }

    @Test
    void shouldDefaultThemeIsDefaultToFalse() {
        final var theme = new Theme();
        assertThat(theme.isDefault()).isFalse();
    }

    @Test
    void shouldAllowNullThemeDescription() {
        final var theme = new Theme();
        theme.setDescription(null);
        assertThat(theme.getDescription()).isNull();
    }

    // ---- TCode ----

    @Test
    void shouldSetAndGetTCodeFields() {
        final var tcode = new TCode();
        tcode.setCode("MM01");
        tcode.setDescription("Create Material");
        tcode.setModule("MM");
        tcode.setRoute("/materials/create");
        tcode.setIcon("add_box");
        tcode.setActive(true);

        assertThat(tcode.getCode()).isEqualTo("MM01");
        assertThat(tcode.getDescription()).isEqualTo("Create Material");
        assertThat(tcode.getModule()).isEqualTo("MM");
        assertThat(tcode.getRoute()).isEqualTo("/materials/create");
        assertThat(tcode.getIcon()).isEqualTo("add_box");
        assertThat(tcode.isActive()).isTrue();
    }

    @Test
    void shouldDefaultTCodeActiveToTrue() {
        final var tcode = new TCode();
        assertThat(tcode.isActive()).isTrue();
    }

    @Test
    void shouldAllowNullTCodeIcon() {
        final var tcode = new TCode();
        tcode.setIcon(null);
        assertThat(tcode.getIcon()).isNull();
    }

    // ---- NamingTemplate ----

    @Test
    void shouldSetAndGetNamingTemplateFields() {
        final var template = new NamingTemplate();
        template.setEntityType("PURCHASE_ORDER");
        template.setPattern("PO-{YYYY}-{SEQ:6}");
        template.setDescription("Purchase order number pattern");
        template.setExample("PO-2025-000001");

        assertThat(template.getEntityType()).isEqualTo("PURCHASE_ORDER");
        assertThat(template.getPattern()).isEqualTo("PO-{YYYY}-{SEQ:6}");
        assertThat(template.getDescription()).isEqualTo("Purchase order number pattern");
        assertThat(template.getExample()).isEqualTo("PO-2025-000001");
    }

    @Test
    void shouldAllowNullNamingTemplateDescription() {
        final var template = new NamingTemplate();
        template.setDescription(null);
        assertThat(template.getDescription()).isNull();
    }

    @Test
    void shouldAllowNullNamingTemplateExample() {
        final var template = new NamingTemplate();
        template.setExample(null);
        assertThat(template.getExample()).isNull();
    }

    // ---- Screen ----

    @Test
    void shouldSetAndGetScreenFields() {
        final var screen = new Screen();
        screen.setScreenId("SCR_MAT_LIST");
        screen.setTitle("Material List");
        screen.setDescription("Lists all materials");
        screen.setModule("MM");
        screen.setTcodeId(5L);
        screen.setScreenType("LIST");

        assertThat(screen.getScreenId()).isEqualTo("SCR_MAT_LIST");
        assertThat(screen.getTitle()).isEqualTo("Material List");
        assertThat(screen.getDescription()).isEqualTo("Lists all materials");
        assertThat(screen.getModule()).isEqualTo("MM");
        assertThat(screen.getTcodeId()).isEqualTo(5L);
        assertThat(screen.getScreenType()).isEqualTo("LIST");
    }

    @Test
    void shouldAllowNullScreenDescription() {
        final var screen = new Screen();
        screen.setDescription(null);
        assertThat(screen.getDescription()).isNull();
    }

    @Test
    void shouldAllowNullScreenTcodeId() {
        final var screen = new Screen();
        screen.setTcodeId(null);
        assertThat(screen.getTcodeId()).isNull();
    }

    // ---- Favourite ----

    @Test
    void shouldSetAndGetFavouriteFields() {
        final var fav = new Favourite();
        fav.setUserId(10L);
        fav.setTcodeId(20L);
        fav.setSortOrder(3);

        assertThat(fav.getUserId()).isEqualTo(10L);
        assertThat(fav.getTcodeId()).isEqualTo(20L);
        assertThat(fav.getSortOrder()).isEqualTo(3);
    }

    @Test
    void shouldDefaultFavouriteSortOrderToZero() {
        final var fav = new Favourite();
        assertThat(fav.getSortOrder()).isZero();
    }

    // ---- RecentNavigation ----

    @Test
    void shouldSetAndGetRecentNavigationFields() {
        final var recent = new RecentNavigation();
        final var now = Instant.now();
        recent.setUserId(10L);
        recent.setTcodeId(20L);
        recent.setAccessedAt(now);

        assertThat(recent.getUserId()).isEqualTo(10L);
        assertThat(recent.getTcodeId()).isEqualTo(20L);
        assertThat(recent.getAccessedAt()).isEqualTo(now);
    }

    // ---- UserPreference ----

    @Test
    void shouldSetAndGetUserPreferenceFields() {
        final var pref = new UserPreference();
        pref.setUserId(10L);
        pref.setThemeId(5L);
        pref.setLocale("de");
        pref.setDateFormat("dd.MM.yyyy");
        pref.setTimeFormat("HH:mm");
        pref.setItemsPerPage(50);

        assertThat(pref.getUserId()).isEqualTo(10L);
        assertThat(pref.getThemeId()).isEqualTo(5L);
        assertThat(pref.getLocale()).isEqualTo("de");
        assertThat(pref.getDateFormat()).isEqualTo("dd.MM.yyyy");
        assertThat(pref.getTimeFormat()).isEqualTo("HH:mm");
        assertThat(pref.getItemsPerPage()).isEqualTo(50);
    }

    @Test
    void shouldDefaultUserPreferenceLocale() {
        final var pref = new UserPreference();
        assertThat(pref.getLocale()).isEqualTo("en");
    }

    @Test
    void shouldDefaultUserPreferenceDateFormat() {
        final var pref = new UserPreference();
        assertThat(pref.getDateFormat()).isEqualTo("yyyy-MM-dd");
    }

    @Test
    void shouldDefaultUserPreferenceTimeFormat() {
        final var pref = new UserPreference();
        assertThat(pref.getTimeFormat()).isEqualTo("HH:mm:ss");
    }

    @Test
    void shouldDefaultUserPreferenceItemsPerPage() {
        final var pref = new UserPreference();
        assertThat(pref.getItemsPerPage()).isEqualTo(20);
    }

    @Test
    void shouldAllowNullUserPreferenceThemeId() {
        final var pref = new UserPreference();
        pref.setThemeId(null);
        assertThat(pref.getThemeId()).isNull();
    }
}
