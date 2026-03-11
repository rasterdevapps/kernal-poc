package com.erp.kernel.plugin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PluginState}.
 */
class PluginStateTest {

    @Test
    void shouldHaveAllValues() {
        assertThat(PluginState.values()).containsExactlyInAnyOrder(
                PluginState.CREATED,
                PluginState.INITIALIZED,
                PluginState.STARTED,
                PluginState.STOPPED,
                PluginState.DESTROYED
        );
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginState.valueOf("CREATED")).isEqualTo(PluginState.CREATED);
        assertThat(PluginState.valueOf("INITIALIZED")).isEqualTo(PluginState.INITIALIZED);
        assertThat(PluginState.valueOf("STARTED")).isEqualTo(PluginState.STARTED);
        assertThat(PluginState.valueOf("STOPPED")).isEqualTo(PluginState.STOPPED);
        assertThat(PluginState.valueOf("DESTROYED")).isEqualTo(PluginState.DESTROYED);
    }
}
