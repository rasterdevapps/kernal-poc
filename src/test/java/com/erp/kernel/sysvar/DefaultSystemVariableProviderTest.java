package com.erp.kernel.sysvar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link DefaultSystemVariableProvider}.
 */
class DefaultSystemVariableProviderTest {

    private DefaultSystemVariableProvider provider;

    @BeforeEach
    void setUp() {
        provider = new DefaultSystemVariableProvider();
    }

    @ParameterizedTest
    @EnumSource(SystemVariable.class)
    void shouldReturnNonNullValue_forAllVariables(final SystemVariable variable) {
        assertThat(provider.getValue(variable)).isNotNull().isNotBlank();
    }

    @Test
    void shouldReturnDateInYyyymmddFormat() {
        final var datum = provider.getValue(SystemVariable.SY_DATUM);
        assertThat(datum).matches("\\d{8}");
    }

    @Test
    void shouldReturnTimeInHhmmssFormat() {
        final var uzeit = provider.getValue(SystemVariable.SY_UZEIT);
        assertThat(uzeit).matches("\\d{6}");
    }

    @Test
    void shouldReturnUserName() {
        final var uname = provider.getValue(SystemVariable.SY_UNAME);
        assertThat(uname).isNotBlank();
    }

    @Test
    void shouldReturnDefaultLanguage() {
        assertThat(provider.getValue(SystemVariable.SY_LANGU)).isEqualTo("E");
    }

    @Test
    void shouldReturnDefaultClient() {
        assertThat(provider.getValue(SystemVariable.SY_MANDT)).isEqualTo("000");
    }

    @Test
    void shouldReturnHostName() {
        assertThat(provider.getValue(SystemVariable.SY_HOST)).isNotBlank();
    }

    @Test
    void shouldReturnSystemId() {
        assertThat(provider.getValue(SystemVariable.SY_SYSID)).isEqualTo("ERP");
    }

    @Test
    void shouldReturnZeroForPageNumber() {
        assertThat(provider.getValue(SystemVariable.SY_PAGNO)).isEqualTo("0");
    }

    @Test
    void shouldReturnZeroForReturnCode() {
        assertThat(provider.getValue(SystemVariable.SY_SUBRC)).isEqualTo("0");
    }

    @Test
    void shouldReturnZeroForLoopIndex() {
        assertThat(provider.getValue(SystemVariable.SY_INDEX)).isEqualTo("0");
    }

    @Test
    void shouldReturnZeroForDbCount() {
        assertThat(provider.getValue(SystemVariable.SY_DBCNT)).isEqualTo("0");
    }

    @Test
    void shouldReturnAllValues() {
        final var allValues = provider.getAllValues();
        assertThat(allValues).hasSize(SystemVariable.values().length);
        for (final var variable : SystemVariable.values()) {
            assertThat(allValues).containsKey(variable);
            assertThat(allValues.get(variable)).isNotNull();
        }
    }

    @Test
    void shouldReturnUnmodifiableMap_fromGetAllValues() {
        final var allValues = provider.getAllValues();
        assertThatThrownBy(() -> allValues.put(SystemVariable.SY_DATUM, "modified"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenVariableIsNull() {
        assertThatThrownBy(() -> provider.getValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldReturnSystem_whenUserNamePropertyIsNull() {
        final var original = System.getProperty("user.name");
        try {
            System.clearProperty("user.name");
            final var result = provider.getValue(SystemVariable.SY_UNAME);
            assertThat(result).isEqualTo("SYSTEM");
        } finally {
            if (original != null) {
                System.setProperty("user.name", original);
            }
        }
    }

    @Test
    void shouldUseProvidedHostName_whenConstructedWithHostName() {
        final var customProvider = new DefaultSystemVariableProvider("UNKNOWN");
        assertThat(customProvider.getValue(SystemVariable.SY_HOST)).isEqualTo("UNKNOWN");
    }

    @Test
    void shouldResolveHostName_viaStaticMethod() {
        final var result = DefaultSystemVariableProvider.resolveHostName();
        assertThat(result).isNotNull().isNotBlank();
    }

    @Test
    void shouldReturnUnknown_whenHostNameCannotBeResolved() {
        try (MockedStatic<InetAddress> mocked = Mockito.mockStatic(InetAddress.class)) {
            mocked.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException("test"));
            final var result = DefaultSystemVariableProvider.resolveHostName();
            assertThat(result).isEqualTo("UNKNOWN");
        }
    }
}
