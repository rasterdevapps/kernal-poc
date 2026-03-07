package com.erp.kernel.datatypes.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link EntityReference}.
 */
class EntityReferenceTest {

    @Test
    void shouldCreateEntityReference_whenValid() {
        final var ref = new EntityReference("Domain", 42L);

        assertThat(ref.entityType()).isEqualTo("Domain");
        assertThat(ref.entityId()).isEqualTo(42L);
        assertThat(ref.getCategory()).isEqualTo(ReferenceCategory.ENTITY);
    }

    @Test
    void shouldThrowNullPointerException_whenEntityTypeIsNull() {
        assertThatThrownBy(() -> new EntityReference(null, 1L))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("entityType");
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIdIsNull() {
        assertThatThrownBy(() -> new EntityReference("Domain", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("entityId");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenEntityTypeIsBlank() {
        assertThatThrownBy(() -> new EntityReference("  ", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("entityType");
    }
}
