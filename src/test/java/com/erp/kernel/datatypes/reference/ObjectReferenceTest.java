package com.erp.kernel.datatypes.reference;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link ObjectReference}.
 */
class ObjectReferenceTest {

    @Test
    void shouldCreateObjectReference_whenValid() {
        final var instance = new Object();
        final var ref = new ObjectReference("com.erp.kernel.SomeClass", instance);

        assertThat(ref.targetClassName()).isEqualTo("com.erp.kernel.SomeClass");
        assertThat(ref.instance()).isSameAs(instance);
        assertThat(ref.isAssigned()).isTrue();
        assertThat(ref.getCategory()).isEqualTo(ReferenceCategory.OBJECT);
    }

    @Test
    void shouldCreateObjectReference_whenInstanceIsNull() {
        final var ref = new ObjectReference("com.erp.kernel.SomeClass", null);

        assertThat(ref.isAssigned()).isFalse();
        assertThat(ref.instance()).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenTargetClassNameIsNull() {
        assertThatThrownBy(() -> new ObjectReference(null, new Object()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("targetClassName");
    }

    @Test
    void shouldThrowIllegalArgumentException_whenTargetClassNameIsBlank() {
        assertThatThrownBy(() -> new ObjectReference("  ", new Object()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("targetClassName");
    }
}
