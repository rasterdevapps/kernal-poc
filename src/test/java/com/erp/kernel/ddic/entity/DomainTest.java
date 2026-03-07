package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link Domain} entity.
 */
class DomainTest {

    @Test
    void shouldSetAndGetDomainName() {
        final var domain = new Domain();
        domain.setDomainName("TEST_DOMAIN");
        assertThat(domain.getDomainName()).isEqualTo("TEST_DOMAIN");
    }

    @Test
    void shouldSetAndGetDataType() {
        final var domain = new Domain();
        domain.setDataType("CHAR");
        assertThat(domain.getDataType()).isEqualTo("CHAR");
    }

    @Test
    void shouldSetAndGetMaxLength() {
        final var domain = new Domain();
        domain.setMaxLength(100);
        assertThat(domain.getMaxLength()).isEqualTo(100);
    }

    @Test
    void shouldSetAndGetDecimalPlaces() {
        final var domain = new Domain();
        domain.setDecimalPlaces(2);
        assertThat(domain.getDecimalPlaces()).isEqualTo(2);
    }

    @Test
    void shouldSetAndGetDescription() {
        final var domain = new Domain();
        domain.setDescription("A test domain");
        assertThat(domain.getDescription()).isEqualTo("A test domain");
    }

    @Test
    void shouldAllowNullMaxLength() {
        final var domain = new Domain();
        domain.setMaxLength(null);
        assertThat(domain.getMaxLength()).isNull();
    }
}
