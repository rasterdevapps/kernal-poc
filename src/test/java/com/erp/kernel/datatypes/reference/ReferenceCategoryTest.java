package com.erp.kernel.datatypes.reference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReferenceCategory}.
 */
class ReferenceCategoryTest {

    @ParameterizedTest
    @EnumSource(ReferenceCategory.class)
    void shouldHaveNonNullDescription(final ReferenceCategory category) {
        assertThat(category.getDescription()).isNotNull().isNotBlank();
    }

    @Test
    void shouldContainAllExpectedCategories() {
        assertThat(ReferenceCategory.values()).hasSize(3);
    }

    @Test
    void shouldHaveDataCategory() {
        assertThat(ReferenceCategory.DATA.getDescription()).contains("data");
    }

    @Test
    void shouldHaveObjectCategory() {
        assertThat(ReferenceCategory.OBJECT.getDescription()).contains("object");
    }

    @Test
    void shouldHaveEntityCategory() {
        assertThat(ReferenceCategory.ENTITY.getDescription()).contains("entity");
    }
}
