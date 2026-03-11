package com.erp.kernel.vertical;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link VerticalType}.
 */
class VerticalTypeTest {

    @ParameterizedTest
    @EnumSource(VerticalType.class)
    void shouldReturnNonBlankDisplayName_whenCalledOnAnyVertical(final VerticalType type) {
        assertThat(type.getDisplayName()).isNotNull().isNotBlank();
    }

    @Test
    void shouldReturnCorrectDisplayName_whenHealthcare() {
        assertThat(VerticalType.HEALTHCARE.getDisplayName())
                .isEqualTo("Healthcare & One Health");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenManufacturing() {
        assertThat(VerticalType.MANUFACTURING.getDisplayName())
                .isEqualTo("Manufacturing & Production");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenRetail() {
        assertThat(VerticalType.RETAIL.getDisplayName())
                .isEqualTo("Retail & Distribution");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenFinance() {
        assertThat(VerticalType.FINANCE.getDisplayName())
                .isEqualTo("Financial Services");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenEducation() {
        assertThat(VerticalType.EDUCATION.getDisplayName())
                .isEqualTo("Education & Academic");
    }

    @Test
    void shouldContainExactlyFiveValues() {
        assertThat(VerticalType.values()).hasSize(5);
    }

    @ParameterizedTest
    @EnumSource(VerticalType.class)
    void shouldResolveFromValueOf_whenGivenValidName(final VerticalType type) {
        assertThat(VerticalType.valueOf(type.name())).isEqualTo(type);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenValueOfReceivesInvalidName() {
        assertThatThrownBy(() -> VerticalType.valueOf("INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
