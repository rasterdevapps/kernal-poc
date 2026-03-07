package com.erp.kernel.businesslogic.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BusinessEventType}.
 */
class BusinessEventTypeTest {

    @ParameterizedTest
    @EnumSource(BusinessEventType.class)
    void shouldHaveNonNullDescription(final BusinessEventType type) {
        assertThat(type.getDescription()).isNotNull().isNotBlank();
    }

    @Test
    void shouldContainAllExpectedTypes() {
        assertThat(BusinessEventType.values()).hasSize(5);
    }
}
