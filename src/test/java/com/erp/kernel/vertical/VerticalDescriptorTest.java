package com.erp.kernel.vertical;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link VerticalDescriptor}.
 */
class VerticalDescriptorTest {

    @Test
    void shouldCreateDescriptor_whenAllFieldsProvided() {
        final var descriptor = new VerticalDescriptor(
                "health-01", "Patient Module", VerticalType.HEALTHCARE,
                "1.0.0", "Patient management", List.of("persistence", "audit"));

        assertThat(descriptor.id()).isEqualTo("health-01");
        assertThat(descriptor.name()).isEqualTo("Patient Module");
        assertThat(descriptor.verticalType()).isEqualTo(VerticalType.HEALTHCARE);
        assertThat(descriptor.version()).isEqualTo("1.0.0");
        assertThat(descriptor.description()).isEqualTo("Patient management");
        assertThat(descriptor.requiredCapabilities()).containsExactly("persistence", "audit");
    }

    @Test
    void shouldReturnId_whenGetIdCalled() {
        final var descriptor = createDescriptor();
        assertThat(descriptor.getId()).isEqualTo("mod-01");
    }

    @Test
    void shouldReturnName_whenGetNameCalled() {
        final var descriptor = createDescriptor();
        assertThat(descriptor.getName()).isEqualTo("Test Module");
    }

    @Test
    void shouldReturnVerticalType_whenGetVerticalTypeCalled() {
        final var descriptor = createDescriptor();
        assertThat(descriptor.getVerticalType()).isEqualTo(VerticalType.MANUFACTURING);
    }

    @Test
    void shouldReturnVersion_whenGetVersionCalled() {
        final var descriptor = createDescriptor();
        assertThat(descriptor.getVersion()).isEqualTo("2.0.0");
    }

    @Test
    void shouldReturnRequiredCapabilities_whenGetRequiredCapabilitiesCalled() {
        final var descriptor = createDescriptor();
        assertThat(descriptor.getRequiredCapabilities()).containsExactly("workflow");
    }

    @Test
    void shouldCreateDefensiveCopy_whenRequiredCapabilitiesProvided() {
        final var mutableList = new ArrayList<>(List.of("cap1", "cap2"));
        final var descriptor = new VerticalDescriptor(
                "id", "name", VerticalType.RETAIL, "1.0.0", "desc", mutableList);
        mutableList.add("cap3");

        assertThat(descriptor.requiredCapabilities()).containsExactly("cap1", "cap2");
    }

    @Test
    void shouldReturnUnmodifiableCapabilitiesList() {
        final var descriptor = createDescriptor();

        assertThatThrownBy(() -> descriptor.requiredCapabilities().add("new-cap"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldAllowEmptyCapabilitiesList() {
        final var descriptor = new VerticalDescriptor(
                "id", "name", VerticalType.FINANCE, "1.0.0", "desc", List.of());

        assertThat(descriptor.requiredCapabilities()).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenIdIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                null, "name", VerticalType.HEALTHCARE, "1.0.0", "desc", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                "id", null, VerticalType.HEALTHCARE, "1.0.0", "desc", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenVerticalTypeIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                "id", "name", null, "1.0.0", "desc", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("verticalType must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenVersionIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                "id", "name", VerticalType.HEALTHCARE, null, "desc", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("version must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                "id", "name", VerticalType.HEALTHCARE, "1.0.0", null, List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("description must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenRequiredCapabilitiesIsNull() {
        assertThatThrownBy(() -> new VerticalDescriptor(
                "id", "name", VerticalType.HEALTHCARE, "1.0.0", "desc", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("requiredCapabilities must not be null");
    }

    @Test
    void shouldBeEqual_whenSameFieldValues() {
        final var descriptor1 = createDescriptor();
        final var descriptor2 = createDescriptor();

        assertThat(descriptor1).isEqualTo(descriptor2);
        assertThat(descriptor1.hashCode()).isEqualTo(descriptor2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenDifferentId() {
        final var descriptor1 = createDescriptor();
        final var descriptor2 = new VerticalDescriptor(
                "other-id", "Test Module", VerticalType.MANUFACTURING,
                "2.0.0", "A test module", List.of("workflow"));

        assertThat(descriptor1).isNotEqualTo(descriptor2);
    }

    @Test
    void shouldReturnMeaningfulToString() {
        final var descriptor = createDescriptor();

        assertThat(descriptor.toString()).contains("mod-01", "Test Module");
    }

    private VerticalDescriptor createDescriptor() {
        return new VerticalDescriptor(
                "mod-01", "Test Module", VerticalType.MANUFACTURING,
                "2.0.0", "A test module", List.of("workflow"));
    }
}
