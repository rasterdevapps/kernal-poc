package com.erp.kernel.plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link ExtensionRegistry}.
 */
class ExtensionRegistryTest {

    private ExtensionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ExtensionRegistry();
    }

    @Test
    void shouldRegisterContribution() {
        final var point = stringExtensionPoint("greeting");

        registry.register(point, "Hello");

        assertThat(registry.getExtensions(point)).containsExactly("Hello");
    }

    @Test
    void shouldRegisterMultipleContributions() {
        final var point = stringExtensionPoint("greeting");

        registry.register(point, "Hello");
        registry.register(point, "Hi");

        assertThat(registry.getExtensions(point)).containsExactly("Hello", "Hi");
    }

    @Test
    void shouldReturnEmptyList_whenNoContributionsRegistered() {
        final var point = stringExtensionPoint("empty");

        final var result = registry.getExtensions(point);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnUnmodifiableList() {
        final var point = stringExtensionPoint("greeting");
        registry.register(point, "Hello");

        final var result = registry.getExtensions(point);

        assertThatThrownBy(() -> result.add("extra"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnTrue_whenExtensionsExist() {
        final var point = stringExtensionPoint("greeting");
        registry.register(point, "Hello");

        assertThat(registry.hasExtensions(point)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenNoExtensionsExist() {
        final var point = stringExtensionPoint("empty");

        assertThat(registry.hasExtensions(point)).isFalse();
    }

    @Test
    void shouldClearAllExtensions() {
        final var point = stringExtensionPoint("greeting");
        registry.register(point, "Hello");

        registry.clear();

        assertThat(registry.hasExtensions(point)).isFalse();
        assertThat(registry.getExtensions(point)).isEmpty();
    }

    @Test
    void shouldKeepExtensionsSeparateByPointName() {
        final var greetingPoint = stringExtensionPoint("greeting");
        final var farewellPoint = stringExtensionPoint("farewell");

        registry.register(greetingPoint, "Hello");
        registry.register(farewellPoint, "Goodbye");

        assertThat(registry.getExtensions(greetingPoint)).containsExactly("Hello");
        assertThat(registry.getExtensions(farewellPoint)).containsExactly("Goodbye");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisterExtensionPointIsNull() {
        assertThatThrownBy(() -> registry.register(null, "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("extensionPoint");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisterContributionIsNull() {
        final var point = stringExtensionPoint("test");

        assertThatThrownBy(() -> registry.register(point, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("contribution");
    }

    @Test
    void shouldThrowNullPointerException_whenGetExtensionsPointIsNull() {
        assertThatThrownBy(() -> registry.getExtensions(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("extensionPoint");
    }

    @Test
    void shouldThrowNullPointerException_whenHasExtensionsPointIsNull() {
        assertThatThrownBy(() -> registry.hasExtensions(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("extensionPoint");
    }

    private static ExtensionPoint<String> stringExtensionPoint(final String name) {
        return new ExtensionPoint<>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        };
    }
}
