package com.erp.kernel.plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link PluginRegistry}.
 */
class PluginRegistryTest {

    private PluginRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new PluginRegistry();
    }

    @Test
    void shouldRegisterPlugin() {
        final var plugin = createPlugin("core", "Core Module", "1.0.0");

        registry.register(plugin);

        assertThat(registry.isRegistered("core")).isTrue();
    }

    @Test
    void shouldFindPluginById() {
        final var plugin = createPlugin("core", "Core Module", "1.0.0");
        registry.register(plugin);

        final var result = registry.findById("core");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("core");
        assertThat(result.get().getName()).isEqualTo("Core Module");
    }

    @Test
    void shouldReturnEmpty_whenPluginNotFound() {
        final var result = registry.findById("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllRegisteredPlugins() {
        registry.register(createPlugin("core", "Core", "1.0.0"));
        registry.register(createPlugin("health", "Health", "2.0.0"));

        final var result = registry.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnUnmodifiableCollection() {
        registry.register(createPlugin("core", "Core", "1.0.0"));

        final var result = registry.getAll();

        assertThatThrownBy(() -> result.add(createPlugin("extra", "Extra", "1.0.0")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnTrue_whenPluginIsRegistered() {
        registry.register(createPlugin("core", "Core", "1.0.0"));

        assertThat(registry.isRegistered("core")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenPluginIsNotRegistered() {
        assertThat(registry.isRegistered("nonexistent")).isFalse();
    }

    @Test
    void shouldClearAllPlugins() {
        registry.register(createPlugin("core", "Core", "1.0.0"));
        registry.register(createPlugin("health", "Health", "2.0.0"));

        registry.clear();

        assertThat(registry.getAll()).isEmpty();
        assertThat(registry.isRegistered("core")).isFalse();
        assertThat(registry.isRegistered("health")).isFalse();
    }

    @Test
    void shouldThrowIllegalArgumentException_whenDuplicatePlugin() {
        registry.register(createPlugin("core", "Core", "1.0.0"));

        assertThatThrownBy(() -> registry.register(
                createPlugin("core", "Core v2", "2.0.0")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("core");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisterNull() {
        assertThatThrownBy(() -> registry.register(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("plugin");
    }

    @Test
    void shouldThrowNullPointerException_whenFindByIdNull() {
        assertThatThrownBy(() -> registry.findById(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
    }

    @Test
    void shouldThrowNullPointerException_whenIsRegisteredNull() {
        assertThatThrownBy(() -> registry.isRegistered(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
    }

    private static Plugin createPlugin(final String id, final String name,
                                       final String version) {
        return new Plugin() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public PluginState getState() {
                return PluginState.CREATED;
            }

            @Override
            public List<String> getDependencies() {
                return List.of();
            }

            @Override
            public void initialize(final PluginContext context) {
                // no-op for testing
            }

            @Override
            public void start() {
                // no-op for testing
            }

            @Override
            public void stop() {
                // no-op for testing
            }

            @Override
            public void destroy() {
                // no-op for testing
            }
        };
    }
}
