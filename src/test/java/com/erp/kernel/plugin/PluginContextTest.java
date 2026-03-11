package com.erp.kernel.plugin;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link PluginContext}.
 */
class PluginContextTest {

    @Test
    void shouldCreateContext_whenAllFieldsProvided() {
        final var properties = Map.of("key", "value");

        final var context = new PluginContext("ERP Kernel", "1.0.0", properties);

        assertThat(context.frameworkName()).isEqualTo("ERP Kernel");
        assertThat(context.frameworkVersion()).isEqualTo("1.0.0");
        assertThat(context.properties()).containsEntry("key", "value");
    }

    @Test
    void shouldCreateContext_whenPropertiesAreEmpty() {
        final var context = new PluginContext("ERP Kernel", "1.0.0", Map.of());

        assertThat(context.properties()).isEmpty();
    }

    @Test
    void shouldReturnImmutableProperties() {
        final var mutableMap = new HashMap<String, String>();
        mutableMap.put("key", "value");

        final var context = new PluginContext("ERP Kernel", "1.0.0", mutableMap);

        assertThatThrownBy(() -> context.properties().put("new", "entry"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotReflectMutationsToOriginalMap() {
        final var mutableMap = new HashMap<String, String>();
        mutableMap.put("key", "value");

        final var context = new PluginContext("ERP Kernel", "1.0.0", mutableMap);
        mutableMap.put("extra", "entry");

        assertThat(context.properties()).hasSize(1);
        assertThat(context.properties()).doesNotContainKey("extra");
    }

    @Test
    void shouldThrowNullPointerException_whenFrameworkNameIsNull() {
        assertThatThrownBy(() -> new PluginContext(null, "1.0.0", Map.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("frameworkName");
    }

    @Test
    void shouldThrowNullPointerException_whenFrameworkVersionIsNull() {
        assertThatThrownBy(() -> new PluginContext("ERP Kernel", null, Map.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("frameworkVersion");
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new PluginContext("ERP Kernel", "1.0.0", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("properties");
    }
}
