package com.erp.kernel.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link ErpModuleRegistry} component.
 */
class ErpModuleRegistryTest {

    private ErpModuleRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ErpModuleRegistry();
    }

    @Test
    void shouldRegisterAndFindModule_whenModuleIsValid() {
        // Arrange
        var module = ErpModuleDescriptor.of("core", "1.0.0", "Core module");

        // Act
        registry.register(module);
        var result = registry.findByName("core");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("core");
        assertThat(result.get().getVersion()).isEqualTo("1.0.0");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDuplicateNameRegistered() {
        // Arrange
        var module1 = ErpModuleDescriptor.of("core", "1.0.0", "Core v1");
        var module2 = ErpModuleDescriptor.of("core", "2.0.0", "Core v2");
        registry.register(module1);

        // Act & Assert
        assertThatThrownBy(() -> registry.register(module2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Module already registered: core");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisteringNull() {
        assertThatThrownBy(() -> registry.register(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("module must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenFindByNameWithNull() {
        assertThatThrownBy(() -> registry.findByName(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldReturnEmptyOptional_whenModuleNotFound() {
        // Act
        var result = registry.findByName("nonexistent");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrue_whenModuleIsRegistered() {
        // Arrange
        var module = ErpModuleDescriptor.of("ddic", "1.0.0", "Data Dictionary");
        registry.register(module);

        // Act & Assert
        assertThat(registry.isRegistered("ddic")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenModuleIsNotRegistered() {
        // Act & Assert
        assertThat(registry.isRegistered("unknown")).isFalse();
    }

    @Test
    void shouldThrowNullPointerException_whenIsRegisteredWithNull() {
        assertThatThrownBy(() -> registry.isRegistered(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldReturnUnmodifiableCollection_whenGetAllCalled() {
        // Arrange
        var module = ErpModuleDescriptor.of("core", "1.0.0", "Core");
        registry.register(module);

        // Act
        var all = registry.getAll();

        // Assert
        assertThat(all).hasSize(1);
        assertThatThrownBy(() -> all.add(ErpModuleDescriptor.of("x", "1", "x")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnEmptyCollection_whenNoModulesRegistered() {
        // Act & Assert
        assertThat(registry.getAll()).isEmpty();
    }

    @Test
    void shouldRemoveAllModules_whenClearCalled() {
        // Arrange
        registry.register(ErpModuleDescriptor.of("m1", "1.0.0", "Module 1"));
        registry.register(ErpModuleDescriptor.of("m2", "1.0.0", "Module 2"));

        // Act
        registry.clear();

        // Assert
        assertThat(registry.getAll()).isEmpty();
        assertThat(registry.isRegistered("m1")).isFalse();
        assertThat(registry.isRegistered("m2")).isFalse();
    }

    @Test
    void shouldReturnMultipleModules_whenSeveralRegistered() {
        // Arrange
        var mod1 = ErpModuleDescriptor.of("core", "1.0.0", "Core");
        var mod2 = new ErpModuleDescriptor(
                "security", "2.0.0", "Security", List.of("core"), true);
        registry.register(mod1);
        registry.register(mod2);

        // Act
        var all = registry.getAll();

        // Assert
        assertThat(all).hasSize(2);
    }
}
