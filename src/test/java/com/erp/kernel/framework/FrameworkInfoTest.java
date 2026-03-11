package com.erp.kernel.framework;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link FrameworkInfo} record.
 */
class FrameworkInfoTest {

    @Test
    void shouldCreateFrameworkInfo_whenAllValuesAreValid() {
        // Arrange
        var capabilities = List.of("Database Abstraction", "Security");

        // Act
        var info = new FrameworkInfo("ERP Kernel", "1.0.0", capabilities);

        // Assert
        assertThat(info.name()).isEqualTo("ERP Kernel");
        assertThat(info.version()).isEqualTo("1.0.0");
        assertThat(info.capabilities()).containsExactly("Database Abstraction", "Security");
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() -> new FrameworkInfo(null, "1.0.0", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenVersionIsNull() {
        assertThatThrownBy(() -> new FrameworkInfo("ERP", null, List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("version must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenCapabilitiesIsNull() {
        assertThatThrownBy(() -> new FrameworkInfo("ERP", "1.0.0", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("capabilities must not be null");
    }

    @Test
    void shouldReturnImmutableCapabilities_whenConstructedWithMutableList() {
        // Arrange
        var mutableList = new ArrayList<>(List.of("cap1", "cap2"));

        // Act
        var info = new FrameworkInfo("ERP", "1.0.0", mutableList);
        mutableList.add("cap3");

        // Assert
        assertThat(info.capabilities()).containsExactly("cap1", "cap2");
        assertThatThrownBy(() -> info.capabilities().add("cap4"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldBeEqual_whenAllFieldsMatch() {
        // Arrange
        var info1 = new FrameworkInfo("ERP", "1.0.0", List.of("cap"));
        var info2 = new FrameworkInfo("ERP", "1.0.0", List.of("cap"));

        // Assert
        assertThat(info1).isEqualTo(info2);
        assertThat(info1.hashCode()).isEqualTo(info2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenFieldsDiffer() {
        // Arrange
        var info1 = new FrameworkInfo("ERP1", "1.0.0", List.of());
        var info2 = new FrameworkInfo("ERP2", "1.0.0", List.of());

        // Assert
        assertThat(info1).isNotEqualTo(info2);
    }

    @Test
    void shouldCreateWithEmptyCapabilities() {
        // Act
        var info = new FrameworkInfo("ERP", "1.0.0", List.of());

        // Assert
        assertThat(info.capabilities()).isEmpty();
    }
}
