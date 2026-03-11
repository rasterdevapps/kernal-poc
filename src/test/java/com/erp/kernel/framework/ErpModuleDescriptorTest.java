package com.erp.kernel.framework;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link ErpModuleDescriptor} record.
 */
class ErpModuleDescriptorTest {

    @Test
    void shouldCreateDescriptor_whenAllValuesAreValid() {
        // Arrange
        var dependencies = List.of("core", "security");

        // Act
        var descriptor = new ErpModuleDescriptor(
                "ddic", "1.0.0", "Data Dictionary", dependencies, true);

        // Assert
        assertThat(descriptor.name()).isEqualTo("ddic");
        assertThat(descriptor.version()).isEqualTo("1.0.0");
        assertThat(descriptor.description()).isEqualTo("Data Dictionary");
        assertThat(descriptor.dependencies()).containsExactly("core", "security");
        assertThat(descriptor.enabled()).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() ->
                new ErpModuleDescriptor(null, "1.0.0", "desc", List.of(), true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenVersionIsNull() {
        assertThatThrownBy(() ->
                new ErpModuleDescriptor("mod", null, "desc", List.of(), true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("version must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        assertThatThrownBy(() ->
                new ErpModuleDescriptor("mod", "1.0.0", null, List.of(), true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("description must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenDependenciesIsNull() {
        assertThatThrownBy(() ->
                new ErpModuleDescriptor("mod", "1.0.0", "desc", null, true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("dependencies must not be null");
    }

    @Test
    void shouldCreateEnabledDescriptorWithNoDependencies_whenUsingOfFactory() {
        // Act
        var descriptor = ErpModuleDescriptor.of("core", "2.0.0", "Core module");

        // Assert
        assertThat(descriptor.name()).isEqualTo("core");
        assertThat(descriptor.version()).isEqualTo("2.0.0");
        assertThat(descriptor.description()).isEqualTo("Core module");
        assertThat(descriptor.dependencies()).isEmpty();
        assertThat(descriptor.enabled()).isTrue();
    }

    @Test
    void shouldReturnImmutableDependencies_whenConstructedWithMutableList() {
        // Arrange
        var mutableList = new ArrayList<>(List.of("dep1", "dep2"));

        // Act
        var descriptor = new ErpModuleDescriptor(
                "mod", "1.0.0", "desc", mutableList, true);
        mutableList.add("dep3");

        // Assert
        assertThat(descriptor.dependencies()).containsExactly("dep1", "dep2");
        assertThatThrownBy(() -> descriptor.dependencies().add("dep4"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnCorrectValues_whenCallingErpModuleInterfaceMethods() {
        // Arrange
        var descriptor = new ErpModuleDescriptor(
                "security", "3.0.0", "Security module", List.of("core"), false);

        // Act & Assert
        assertThat(descriptor.getName()).isEqualTo("security");
        assertThat(descriptor.getVersion()).isEqualTo("3.0.0");
        assertThat(descriptor.getDescription()).isEqualTo("Security module");
        assertThat(descriptor.getDependencies()).containsExactly("core");
        assertThat(descriptor.isEnabled()).isFalse();
    }

    @Test
    void shouldImplementErpModuleInterface() {
        // Arrange
        var descriptor = ErpModuleDescriptor.of("mod", "1.0.0", "desc");

        // Assert
        assertThat(descriptor).isInstanceOf(ErpModule.class);
    }

    @Test
    void shouldBeEqual_whenAllFieldsMatch() {
        // Arrange
        var descriptor1 = new ErpModuleDescriptor(
                "mod", "1.0.0", "desc", List.of("dep"), true);
        var descriptor2 = new ErpModuleDescriptor(
                "mod", "1.0.0", "desc", List.of("dep"), true);

        // Assert
        assertThat(descriptor1).isEqualTo(descriptor2);
        assertThat(descriptor1.hashCode()).isEqualTo(descriptor2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenFieldsDiffer() {
        // Arrange
        var descriptor1 = ErpModuleDescriptor.of("mod1", "1.0.0", "desc");
        var descriptor2 = ErpModuleDescriptor.of("mod2", "1.0.0", "desc");

        // Assert
        assertThat(descriptor1).isNotEqualTo(descriptor2);
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        var descriptor = ErpModuleDescriptor.of("core", "1.0.0", "Core");

        // Act
        var result = descriptor.toString();

        // Assert
        assertThat(result).contains("core");
        assertThat(result).contains("1.0.0");
        assertThat(result).contains("Core");
    }
}
