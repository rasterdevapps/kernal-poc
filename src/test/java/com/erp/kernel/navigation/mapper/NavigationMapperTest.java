package com.erp.kernel.navigation.mapper;

import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.entity.Favourite;
import com.erp.kernel.navigation.entity.NamingTemplate;
import com.erp.kernel.navigation.entity.RecentNavigation;
import com.erp.kernel.navigation.entity.Screen;
import com.erp.kernel.navigation.entity.TCode;
import com.erp.kernel.navigation.entity.Theme;
import com.erp.kernel.navigation.entity.UserPreference;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive tests for all navigation mapper classes.
 */
class NavigationMapperTest {

    // =====================================================================
    // ThemeMapper
    // =====================================================================

    @Test
    void shouldConvertThemeEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new Theme();
        entity.setId(1L);
        entity.setThemeName("Dark Mode");
        entity.setDescription("A dark theme");
        entity.setPrimaryColor("#000000");
        entity.setSecondaryColor("#FFFFFF");
        entity.setActive(true);
        entity.setDefault(true);
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-01-02T00:00:00Z"));

        // Act
        final var result = ThemeMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.themeName()).isEqualTo("Dark Mode");
        assertThat(result.description()).isEqualTo("A dark theme");
        assertThat(result.primaryColor()).isEqualTo("#000000");
        assertThat(result.secondaryColor()).isEqualTo("#FFFFFF");
        assertThat(result.active()).isTrue();
        assertThat(result.isDefault()).isTrue();
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-01-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-01-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenThemeToDtoEntityIsNull() {
        assertThatThrownBy(() -> ThemeMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateThemeRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateThemeRequest("Light Mode", "A light theme", "#FFFFFF", "#000000", true);

        // Act
        final var result = ThemeMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getThemeName()).isEqualTo("Light Mode");
        assertThat(result.getDescription()).isEqualTo("A light theme");
        assertThat(result.getPrimaryColor()).isEqualTo("#FFFFFF");
        assertThat(result.getSecondaryColor()).isEqualTo("#000000");
        assertThat(result.isDefault()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenThemeToEntityRequestIsNull() {
        assertThatThrownBy(() -> ThemeMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateThemeEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new Theme();
        entity.setThemeName("Original");
        entity.setDescription("Old description");
        entity.setPrimaryColor("#111111");
        entity.setSecondaryColor("#222222");
        entity.setDefault(false);

        final var request = new CreateThemeRequest("New Name", "New description", "#333333", "#444444", true);

        // Act
        ThemeMapper.updateEntity(entity, request);

        // Assert — themeName is NOT updated (unique key)
        assertThat(entity.getThemeName()).isEqualTo("Original");
        assertThat(entity.getDescription()).isEqualTo("New description");
        assertThat(entity.getPrimaryColor()).isEqualTo("#333333");
        assertThat(entity.getSecondaryColor()).isEqualTo("#444444");
        assertThat(entity.isDefault()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenThemeUpdateEntityIsNull() {
        final var request = new CreateThemeRequest("Name", "Desc", "#000000", "#FFFFFF", false);
        assertThatThrownBy(() -> ThemeMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenThemeUpdateRequestIsNull() {
        final var entity = new Theme();
        assertThatThrownBy(() -> ThemeMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateThemeMapper() throws Exception {
        final var constructor = ThemeMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // TCodeMapper
    // =====================================================================

    @Test
    void shouldConvertTCodeEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new TCode();
        entity.setId(2L);
        entity.setCode("SU01");
        entity.setDescription("User Maintenance");
        entity.setModule("SECURITY");
        entity.setRoute("/security/users");
        entity.setIcon("user-icon");
        entity.setActive(true);
        entity.setCreatedAt(Instant.parse("2025-02-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-02-02T00:00:00Z"));

        // Act
        final var result = TCodeMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.code()).isEqualTo("SU01");
        assertThat(result.description()).isEqualTo("User Maintenance");
        assertThat(result.module()).isEqualTo("SECURITY");
        assertThat(result.route()).isEqualTo("/security/users");
        assertThat(result.icon()).isEqualTo("user-icon");
        assertThat(result.active()).isTrue();
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-02-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-02-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenTCodeToDtoEntityIsNull() {
        assertThatThrownBy(() -> TCodeMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateTCodeRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateTCodeRequest("MM01", "Create Material", "MM", "/mm/materials/create", "box-icon");

        // Act
        final var result = TCodeMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("MM01");
        assertThat(result.getDescription()).isEqualTo("Create Material");
        assertThat(result.getModule()).isEqualTo("MM");
        assertThat(result.getRoute()).isEqualTo("/mm/materials/create");
        assertThat(result.getIcon()).isEqualTo("box-icon");
    }

    @Test
    void shouldThrowNullPointerException_whenTCodeToEntityRequestIsNull() {
        assertThatThrownBy(() -> TCodeMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateTCodeEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new TCode();
        entity.setCode("SU01");
        entity.setDescription("Old description");
        entity.setModule("OLD_MODULE");
        entity.setRoute("/old/route");
        entity.setIcon("old-icon");

        final var request = new CreateTCodeRequest("NEW_CODE", "New description", "NEW_MODULE", "/new/route", "new-icon");

        // Act
        TCodeMapper.updateEntity(entity, request);

        // Assert — code is NOT updated (unique key)
        assertThat(entity.getCode()).isEqualTo("SU01");
        assertThat(entity.getDescription()).isEqualTo("New description");
        assertThat(entity.getModule()).isEqualTo("NEW_MODULE");
        assertThat(entity.getRoute()).isEqualTo("/new/route");
        assertThat(entity.getIcon()).isEqualTo("new-icon");
    }

    @Test
    void shouldThrowNullPointerException_whenTCodeUpdateEntityIsNull() {
        final var request = new CreateTCodeRequest("MM01", "Desc", "MM", "/route", "icon");
        assertThatThrownBy(() -> TCodeMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenTCodeUpdateRequestIsNull() {
        final var entity = new TCode();
        assertThatThrownBy(() -> TCodeMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateTCodeMapper() throws Exception {
        final var constructor = TCodeMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // NamingTemplateMapper
    // =====================================================================

    @Test
    void shouldConvertNamingTemplateEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new NamingTemplate();
        entity.setId(3L);
        entity.setEntityType("SCREEN");
        entity.setPattern("SCR-{module}-{seq}");
        entity.setDescription("Screen naming template");
        entity.setExample("SCR-MM-001");
        entity.setCreatedAt(Instant.parse("2025-03-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-03-02T00:00:00Z"));

        // Act
        final var result = NamingTemplateMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.entityType()).isEqualTo("SCREEN");
        assertThat(result.pattern()).isEqualTo("SCR-{module}-{seq}");
        assertThat(result.description()).isEqualTo("Screen naming template");
        assertThat(result.example()).isEqualTo("SCR-MM-001");
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-03-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-03-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenNamingTemplateToDtoEntityIsNull() {
        assertThatThrownBy(() -> NamingTemplateMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateNamingTemplateRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateNamingTemplateRequest("TCODE", "TC-{module}-{seq}", "TCode template", "TC-FI-001");

        // Act
        final var result = NamingTemplateMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEntityType()).isEqualTo("TCODE");
        assertThat(result.getPattern()).isEqualTo("TC-{module}-{seq}");
        assertThat(result.getDescription()).isEqualTo("TCode template");
        assertThat(result.getExample()).isEqualTo("TC-FI-001");
    }

    @Test
    void shouldThrowNullPointerException_whenNamingTemplateToEntityRequestIsNull() {
        assertThatThrownBy(() -> NamingTemplateMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateNamingTemplateEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new NamingTemplate();
        entity.setEntityType("SCREEN");
        entity.setPattern("OLD-{seq}");
        entity.setDescription("Old description");
        entity.setExample("OLD-001");

        final var request = new CreateNamingTemplateRequest("API", "NEW-{seq}", "New description", "NEW-001");

        // Act
        NamingTemplateMapper.updateEntity(entity, request);

        // Assert — entityType is NOT updated (unique key)
        assertThat(entity.getEntityType()).isEqualTo("SCREEN");
        assertThat(entity.getPattern()).isEqualTo("NEW-{seq}");
        assertThat(entity.getDescription()).isEqualTo("New description");
        assertThat(entity.getExample()).isEqualTo("NEW-001");
    }

    @Test
    void shouldThrowNullPointerException_whenNamingTemplateUpdateEntityIsNull() {
        final var request = new CreateNamingTemplateRequest("SCREEN", "pattern", "desc", "example");
        assertThatThrownBy(() -> NamingTemplateMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenNamingTemplateUpdateRequestIsNull() {
        final var entity = new NamingTemplate();
        assertThatThrownBy(() -> NamingTemplateMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateNamingTemplateMapper() throws Exception {
        final var constructor = NamingTemplateMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // ScreenMapper
    // =====================================================================

    @Test
    void shouldConvertScreenEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new Screen();
        entity.setId(4L);
        entity.setScreenId("SCR-MM-001");
        entity.setTitle("Material List");
        entity.setDescription("Displays all materials");
        entity.setModule("MM");
        entity.setTcodeId(10L);
        entity.setScreenType("LIST");
        entity.setCreatedAt(Instant.parse("2025-04-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-04-02T00:00:00Z"));

        // Act
        final var result = ScreenMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(4L);
        assertThat(result.screenId()).isEqualTo("SCR-MM-001");
        assertThat(result.title()).isEqualTo("Material List");
        assertThat(result.description()).isEqualTo("Displays all materials");
        assertThat(result.module()).isEqualTo("MM");
        assertThat(result.tcodeId()).isEqualTo(10L);
        assertThat(result.screenType()).isEqualTo("LIST");
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-04-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-04-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenScreenToDtoEntityIsNull() {
        assertThatThrownBy(() -> ScreenMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateScreenRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateScreenRequest("SCR-FI-001", "Finance Dashboard", "Main finance screen", "FI", 20L, "DASHBOARD");

        // Act
        final var result = ScreenMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getScreenId()).isEqualTo("SCR-FI-001");
        assertThat(result.getTitle()).isEqualTo("Finance Dashboard");
        assertThat(result.getDescription()).isEqualTo("Main finance screen");
        assertThat(result.getModule()).isEqualTo("FI");
        assertThat(result.getTcodeId()).isEqualTo(20L);
        assertThat(result.getScreenType()).isEqualTo("DASHBOARD");
    }

    @Test
    void shouldThrowNullPointerException_whenScreenToEntityRequestIsNull() {
        assertThatThrownBy(() -> ScreenMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateScreenEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new Screen();
        entity.setScreenId("SCR-MM-001");
        entity.setTitle("Old Title");
        entity.setDescription("Old description");
        entity.setModule("OLD");
        entity.setTcodeId(10L);
        entity.setScreenType("LIST");

        final var request = new CreateScreenRequest("SCR-NEW-001", "New Title", "New description", "NEW", 30L, "FORM");

        // Act
        ScreenMapper.updateEntity(entity, request);

        // Assert — screenId is NOT updated (unique key)
        assertThat(entity.getScreenId()).isEqualTo("SCR-MM-001");
        assertThat(entity.getTitle()).isEqualTo("New Title");
        assertThat(entity.getDescription()).isEqualTo("New description");
        assertThat(entity.getModule()).isEqualTo("NEW");
        assertThat(entity.getTcodeId()).isEqualTo(30L);
        assertThat(entity.getScreenType()).isEqualTo("FORM");
    }

    @Test
    void shouldThrowNullPointerException_whenScreenUpdateEntityIsNull() {
        final var request = new CreateScreenRequest("SCR-01", "Title", "Desc", "MM", 1L, "LIST");
        assertThatThrownBy(() -> ScreenMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenScreenUpdateRequestIsNull() {
        final var entity = new Screen();
        assertThatThrownBy(() -> ScreenMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateScreenMapper() throws Exception {
        final var constructor = ScreenMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // FavouriteMapper
    // =====================================================================

    @Test
    void shouldConvertFavouriteEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new Favourite();
        entity.setId(5L);
        entity.setUserId(100L);
        entity.setTcodeId(200L);
        entity.setSortOrder(3);
        entity.setCreatedAt(Instant.parse("2025-05-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-05-02T00:00:00Z"));

        // Act
        final var result = FavouriteMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.userId()).isEqualTo(100L);
        assertThat(result.tcodeId()).isEqualTo(200L);
        assertThat(result.sortOrder()).isEqualTo(3);
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-05-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-05-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenFavouriteToDtoEntityIsNull() {
        assertThatThrownBy(() -> FavouriteMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateFavouriteRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateFavouriteRequest(101L, 201L, 5);

        // Act
        final var result = FavouriteMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(101L);
        assertThat(result.getTcodeId()).isEqualTo(201L);
        assertThat(result.getSortOrder()).isEqualTo(5);
    }

    @Test
    void shouldThrowNullPointerException_whenFavouriteToEntityRequestIsNull() {
        assertThatThrownBy(() -> FavouriteMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateFavouriteEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new Favourite();
        entity.setUserId(100L);
        entity.setTcodeId(200L);
        entity.setSortOrder(1);

        final var request = new CreateFavouriteRequest(999L, 888L, 10);

        // Act
        FavouriteMapper.updateEntity(entity, request);

        // Assert — only sortOrder is updated
        assertThat(entity.getUserId()).isEqualTo(100L);
        assertThat(entity.getTcodeId()).isEqualTo(200L);
        assertThat(entity.getSortOrder()).isEqualTo(10);
    }

    @Test
    void shouldThrowNullPointerException_whenFavouriteUpdateEntityIsNull() {
        final var request = new CreateFavouriteRequest(1L, 2L, 0);
        assertThatThrownBy(() -> FavouriteMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenFavouriteUpdateRequestIsNull() {
        final var entity = new Favourite();
        assertThatThrownBy(() -> FavouriteMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateFavouriteMapper() throws Exception {
        final var constructor = FavouriteMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // RecentNavigationMapper
    // =====================================================================

    @Test
    void shouldConvertRecentNavigationEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new RecentNavigation();
        entity.setId(6L);
        entity.setUserId(100L);
        entity.setTcodeId(200L);
        entity.setAccessedAt(Instant.parse("2025-06-01T12:00:00Z"));
        entity.setCreatedAt(Instant.parse("2025-06-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-06-02T00:00:00Z"));

        // Act
        final var result = RecentNavigationMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(6L);
        assertThat(result.userId()).isEqualTo(100L);
        assertThat(result.tcodeId()).isEqualTo(200L);
        assertThat(result.accessedAt()).isEqualTo(Instant.parse("2025-06-01T12:00:00Z"));
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-06-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-06-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenRecentNavigationToDtoEntityIsNull() {
        assertThatThrownBy(() -> RecentNavigationMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateRecentNavigationRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var before = Instant.now();
        final var request = new CreateRecentNavigationRequest(102L, 202L);

        // Act
        final var result = RecentNavigationMapper.toEntity(request);

        // Assert
        final var after = Instant.now();
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(102L);
        assertThat(result.getTcodeId()).isEqualTo(202L);
        assertThat(result.getAccessedAt()).isNotNull();
        assertThat(result.getAccessedAt()).isBetween(before, after.plus(Duration.ofSeconds(1)));
    }

    @Test
    void shouldThrowNullPointerException_whenRecentNavigationToEntityRequestIsNull() {
        assertThatThrownBy(() -> RecentNavigationMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateRecentNavigationMapper() throws Exception {
        final var constructor = RecentNavigationMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    // =====================================================================
    // UserPreferenceMapper
    // =====================================================================

    @Test
    void shouldConvertUserPreferenceEntityToDto_whenEntityIsValid() {
        // Arrange
        final var entity = new UserPreference();
        entity.setId(7L);
        entity.setUserId(100L);
        entity.setThemeId(50L);
        entity.setLocale("en");
        entity.setDateFormat("yyyy-MM-dd");
        entity.setTimeFormat("HH:mm:ss");
        entity.setItemsPerPage(25);
        entity.setCreatedAt(Instant.parse("2025-07-01T00:00:00Z"));
        entity.setUpdatedAt(Instant.parse("2025-07-02T00:00:00Z"));

        // Act
        final var result = UserPreferenceMapper.toDto(entity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(7L);
        assertThat(result.userId()).isEqualTo(100L);
        assertThat(result.themeId()).isEqualTo(50L);
        assertThat(result.locale()).isEqualTo("en");
        assertThat(result.dateFormat()).isEqualTo("yyyy-MM-dd");
        assertThat(result.timeFormat()).isEqualTo("HH:mm:ss");
        assertThat(result.itemsPerPage()).isEqualTo(25);
        assertThat(result.createdAt()).isEqualTo(Instant.parse("2025-07-01T00:00:00Z"));
        assertThat(result.updatedAt()).isEqualTo(Instant.parse("2025-07-02T00:00:00Z"));
    }

    @Test
    void shouldThrowNullPointerException_whenUserPreferenceToDtoEntityIsNull() {
        assertThatThrownBy(() -> UserPreferenceMapper.toDto(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldConvertCreateUserPreferenceRequestToEntity_whenRequestIsValid() {
        // Arrange
        final var request = new CreateUserPreferenceRequest(103L, 51L, "de", "dd.MM.yyyy", "HH:mm", 50);

        // Act
        final var result = UserPreferenceMapper.toEntity(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(103L);
        assertThat(result.getThemeId()).isEqualTo(51L);
        assertThat(result.getLocale()).isEqualTo("de");
        assertThat(result.getDateFormat()).isEqualTo("dd.MM.yyyy");
        assertThat(result.getTimeFormat()).isEqualTo("HH:mm");
        assertThat(result.getItemsPerPage()).isEqualTo(50);
    }

    @Test
    void shouldThrowNullPointerException_whenUserPreferenceToEntityRequestIsNull() {
        assertThatThrownBy(() -> UserPreferenceMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldUpdateUserPreferenceEntityFromRequest_whenBothAreValid() {
        // Arrange
        final var entity = new UserPreference();
        entity.setUserId(100L);
        entity.setThemeId(50L);
        entity.setLocale("en");
        entity.setDateFormat("yyyy-MM-dd");
        entity.setTimeFormat("HH:mm:ss");
        entity.setItemsPerPage(25);

        final var request = new CreateUserPreferenceRequest(999L, 60L, "fr", "dd/MM/yyyy", "hh:mm a", 100);

        // Act
        UserPreferenceMapper.updateEntity(entity, request);

        // Assert — userId is NOT updated (unique key)
        assertThat(entity.getUserId()).isEqualTo(100L);
        assertThat(entity.getThemeId()).isEqualTo(60L);
        assertThat(entity.getLocale()).isEqualTo("fr");
        assertThat(entity.getDateFormat()).isEqualTo("dd/MM/yyyy");
        assertThat(entity.getTimeFormat()).isEqualTo("hh:mm a");
        assertThat(entity.getItemsPerPage()).isEqualTo(100);
    }

    @Test
    void shouldThrowNullPointerException_whenUserPreferenceUpdateEntityIsNull() {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en", "yyyy-MM-dd", "HH:mm", 10);
        assertThatThrownBy(() -> UserPreferenceMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("entity must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenUserPreferenceUpdateRequestIsNull() {
        final var entity = new UserPreference();
        assertThatThrownBy(() -> UserPreferenceMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request must not be null");
    }

    @Test
    void shouldNotInstantiateUserPreferenceMapper() throws Exception {
        final var constructor = UserPreferenceMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }
}
