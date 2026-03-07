package com.erp.kernel.sysvar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SystemVariableDto}.
 */
class SystemVariableDtoTest {

    @Test
    void shouldCreateDto() {
        final var dto = new SystemVariableDto("SY-DATUM", "20260307", "Current date", "DATS");

        assertThat(dto.variableName()).isEqualTo("SY-DATUM");
        assertThat(dto.value()).isEqualTo("20260307");
        assertThat(dto.description()).isEqualTo("Current date");
        assertThat(dto.dataType()).isEqualTo("DATS");
    }
}
