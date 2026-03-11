package com.erp.kernel.vertical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link VerticalRegistry}.
 */
class VerticalRegistryTest {

    private VerticalRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new VerticalRegistry();
    }

    @Test
    void shouldRegisterModule_whenModuleIsValid() {
        final var module = createHealthcareModule();

        registry.register(module);

        assertThat(registry.isRegistered("health-01")).isTrue();
    }

    @Test
    void shouldFindModuleById_whenModuleIsRegistered() {
        final var module = createHealthcareModule();
        registry.register(module);

        final var result = registry.findById("health-01");

        assertThat(result).isPresent().contains(module);
    }

    @Test
    void shouldReturnEmpty_whenModuleNotRegistered() {
        final var result = registry.findById("non-existent");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindModulesByVerticalType_whenModulesExist() {
        final var healthModule = createHealthcareModule();
        final var mfgModule = createManufacturingModule();
        registry.register(healthModule);
        registry.register(mfgModule);

        final var result = registry.findByVerticalType(VerticalType.HEALTHCARE);

        assertThat(result).hasSize(1).containsExactly(healthModule);
    }

    @Test
    void shouldReturnEmptyList_whenNoModulesMatchVerticalType() {
        final var module = createHealthcareModule();
        registry.register(module);

        final var result = registry.findByVerticalType(VerticalType.FINANCE);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllRegisteredModules_whenGetAllCalled() {
        final var healthModule = createHealthcareModule();
        final var mfgModule = createManufacturingModule();
        registry.register(healthModule);
        registry.register(mfgModule);

        final var result = registry.getAll();

        assertThat(result).hasSize(2).contains(healthModule, mfgModule);
    }

    @Test
    void shouldReturnUnmodifiableCollection_whenGetAllCalled() {
        registry.register(createHealthcareModule());

        final var result = registry.getAll();

        assertThatThrownBy(() -> result.add(createManufacturingModule()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnTrue_whenModuleIsRegistered() {
        registry.register(createHealthcareModule());

        assertThat(registry.isRegistered("health-01")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenModuleIsNotRegistered() {
        assertThat(registry.isRegistered("non-existent")).isFalse();
    }

    @Test
    void shouldClearAllModules_whenClearCalled() {
        registry.register(createHealthcareModule());
        registry.register(createManufacturingModule());

        registry.clear();

        assertThat(registry.getAll()).isEmpty();
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDuplicateIdRegistered() {
        registry.register(createHealthcareModule());

        final var duplicate = new VerticalDescriptor(
                "health-01", "Duplicate Module", VerticalType.HEALTHCARE,
                "2.0.0", "Duplicate", List.of());

        assertThatThrownBy(() -> registry.register(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("health-01");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisteringNullModule() {
        assertThatThrownBy(() -> registry.register(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("module must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenFindByIdReceivesNull() {
        assertThatThrownBy(() -> registry.findById(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenFindByVerticalTypeReceivesNull() {
        assertThatThrownBy(() -> registry.findByVerticalType(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("verticalType must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenIsRegisteredReceivesNull() {
        assertThatThrownBy(() -> registry.isRegistered(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldReturnEmptyCollection_whenNoModulesRegistered() {
        assertThat(registry.getAll()).isEmpty();
    }

    @Test
    void shouldReturnMultipleModules_whenSameVerticalTypeRegistered() {
        final var module1 = new VerticalDescriptor(
                "health-01", "Patient Module", VerticalType.HEALTHCARE,
                "1.0.0", "Patient management", List.of("persistence"));
        final var module2 = new VerticalDescriptor(
                "health-02", "Lab Module", VerticalType.HEALTHCARE,
                "1.0.0", "Lab results", List.of("reporting"));
        registry.register(module1);
        registry.register(module2);

        final var result = registry.findByVerticalType(VerticalType.HEALTHCARE);

        assertThat(result).hasSize(2).contains(module1, module2);
    }

    private VerticalDescriptor createHealthcareModule() {
        return new VerticalDescriptor(
                "health-01", "Patient Module", VerticalType.HEALTHCARE,
                "1.0.0", "Patient management", List.of("persistence", "audit"));
    }

    private VerticalDescriptor createManufacturingModule() {
        return new VerticalDescriptor(
                "mfg-01", "Production Module", VerticalType.MANUFACTURING,
                "1.0.0", "Production planning", List.of("workflow"));
    }
}
