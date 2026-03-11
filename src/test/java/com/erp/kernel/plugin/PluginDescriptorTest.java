package com.erp.kernel.plugin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link PluginDescriptor}.
 */
class PluginDescriptorTest {

    @Test
    void shouldCreateDescriptor_whenAllFieldsProvided() {
        final var descriptor = new PluginDescriptor(
                "health", "One Health", "2.0.0",
                "Healthcare module", List.of("core"), PluginState.CREATED);

        assertThat(descriptor.id()).isEqualTo("health");
        assertThat(descriptor.name()).isEqualTo("One Health");
        assertThat(descriptor.version()).isEqualTo("2.0.0");
        assertThat(descriptor.description()).isEqualTo("Healthcare module");
        assertThat(descriptor.dependencies()).containsExactly("core");
        assertThat(descriptor.state()).isEqualTo(PluginState.CREATED);
    }

    @Test
    void shouldCreateDescriptor_whenDependenciesAreEmpty() {
        final var descriptor = new PluginDescriptor(
                "core", "Core", "1.0.0",
                "Core module", List.of(), PluginState.STARTED);

        assertThat(descriptor.dependencies()).isEmpty();
    }

    @Test
    void shouldReturnImmutableDependencies() {
        final var descriptor = new PluginDescriptor(
                "health", "One Health", "2.0.0",
                "Healthcare module", List.of("core"), PluginState.CREATED);

        assertThatThrownBy(() -> descriptor.dependencies().add("extra"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotReflectMutationsToOriginalDependencyList() {
        final var mutableDeps = new ArrayList<String>();
        mutableDeps.add("core");

        final var descriptor = new PluginDescriptor(
                "health", "One Health", "2.0.0",
                "Healthcare module", mutableDeps, PluginState.CREATED);
        mutableDeps.add("extra");

        assertThat(descriptor.dependencies()).hasSize(1);
        assertThat(descriptor.dependencies()).doesNotContain("extra");
    }

    @Test
    void shouldThrowNullPointerException_whenIdIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                null, "Name", "1.0.0", "Desc", List.of(), PluginState.CREATED))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                "id", null, "1.0.0", "Desc", List.of(), PluginState.CREATED))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("name");
    }

    @Test
    void shouldThrowNullPointerException_whenVersionIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                "id", "Name", null, "Desc", List.of(), PluginState.CREATED))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("version");
    }

    @Test
    void shouldThrowNullPointerException_whenDescriptionIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                "id", "Name", "1.0.0", null, List.of(), PluginState.CREATED))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("description");
    }

    @Test
    void shouldThrowNullPointerException_whenDependenciesIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                "id", "Name", "1.0.0", "Desc", null, PluginState.CREATED))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("dependencies");
    }

    @Test
    void shouldThrowNullPointerException_whenStateIsNull() {
        assertThatThrownBy(() -> new PluginDescriptor(
                "id", "Name", "1.0.0", "Desc", List.of(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("state");
    }

    @Test
    void shouldCreateDescriptorFromPlugin() {
        final var plugin = new TestPlugin(
                "retail", "Retail", "3.0.0",
                PluginState.STARTED, List.of("core", "inventory"));

        final var descriptor = PluginDescriptor.from(plugin, "Retail vertical module");

        assertThat(descriptor.id()).isEqualTo("retail");
        assertThat(descriptor.name()).isEqualTo("Retail");
        assertThat(descriptor.version()).isEqualTo("3.0.0");
        assertThat(descriptor.description()).isEqualTo("Retail vertical module");
        assertThat(descriptor.dependencies()).containsExactly("core", "inventory");
        assertThat(descriptor.state()).isEqualTo(PluginState.STARTED);
    }

    @Test
    void shouldThrowNullPointerException_whenFromPluginIsNull() {
        assertThatThrownBy(() -> PluginDescriptor.from(null, "desc"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("plugin");
    }

    @Test
    void shouldThrowNullPointerException_whenFromDescriptionIsNull() {
        final var plugin = new TestPlugin(
                "core", "Core", "1.0.0", PluginState.CREATED, List.of());

        assertThatThrownBy(() -> PluginDescriptor.from(plugin, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("description");
    }

    /**
     * Simple plugin implementation for testing.
     */
    private static class TestPlugin implements Plugin {

        private final String id;
        private final String name;
        private final String version;
        private final PluginState state;
        private final List<String> dependencies;

        TestPlugin(final String id, final String name, final String version,
                   final PluginState state, final List<String> dependencies) {
            this.id = id;
            this.name = name;
            this.version = version;
            this.state = state;
            this.dependencies = dependencies;
        }

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
            return state;
        }

        @Override
        public List<String> getDependencies() {
            return dependencies;
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
    }
}
