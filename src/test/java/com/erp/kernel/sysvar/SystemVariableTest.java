package com.erp.kernel.sysvar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SystemVariable}.
 */
class SystemVariableTest {

    @ParameterizedTest
    @EnumSource(SystemVariable.class)
    void shouldHaveNonNullVariableName(final SystemVariable variable) {
        assertThat(variable.getVariableName()).isNotNull().isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(SystemVariable.class)
    void shouldHaveNonNullDescription(final SystemVariable variable) {
        assertThat(variable.getDescription()).isNotNull().isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(SystemVariable.class)
    void shouldHaveNonNullDataType(final SystemVariable variable) {
        assertThat(variable.getDataType()).isNotNull().isNotBlank();
    }

    @Test
    void shouldContainAllExpectedVariables() {
        assertThat(SystemVariable.values()).hasSize(11);
    }

    @Test
    void shouldHaveSyDatum() {
        assertThat(SystemVariable.SY_DATUM.getVariableName()).isEqualTo("SY-DATUM");
        assertThat(SystemVariable.SY_DATUM.getDataType()).isEqualTo("DATS");
    }

    @Test
    void shouldHaveSyUzeit() {
        assertThat(SystemVariable.SY_UZEIT.getVariableName()).isEqualTo("SY-UZEIT");
        assertThat(SystemVariable.SY_UZEIT.getDataType()).isEqualTo("TIMS");
    }

    @Test
    void shouldHaveSyUname() {
        assertThat(SystemVariable.SY_UNAME.getVariableName()).isEqualTo("SY-UNAME");
        assertThat(SystemVariable.SY_UNAME.getDataType()).isEqualTo("CHAR");
    }

    @Test
    void shouldHaveSyLangu() {
        assertThat(SystemVariable.SY_LANGU.getVariableName()).isEqualTo("SY-LANGU");
        assertThat(SystemVariable.SY_LANGU.getDataType()).isEqualTo("LANG");
    }

    @Test
    void shouldHaveSyMandt() {
        assertThat(SystemVariable.SY_MANDT.getVariableName()).isEqualTo("SY-MANDT");
        assertThat(SystemVariable.SY_MANDT.getDataType()).isEqualTo("CLNT");
    }
}
