package com.erp.kernel.ddic.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link DataElement} entity.
 */
class DataElementTest {

    @Test
    void shouldSetAndGetElementName() {
        final var element = new DataElement();
        element.setElementName("CUSTOMER_NAME");
        assertThat(element.getElementName()).isEqualTo("CUSTOMER_NAME");
    }

    @Test
    void shouldSetAndGetDomain() {
        final var domain = new Domain();
        domain.setDomainName("CHAR_30");
        final var element = new DataElement();
        element.setDomain(domain);
        assertThat(element.getDomain()).isEqualTo(domain);
    }

    @Test
    void shouldSetAndGetShortLabel() {
        final var element = new DataElement();
        element.setShortLabel("CustName");
        assertThat(element.getShortLabel()).isEqualTo("CustName");
    }

    @Test
    void shouldSetAndGetMediumLabel() {
        final var element = new DataElement();
        element.setMediumLabel("Customer Name");
        assertThat(element.getMediumLabel()).isEqualTo("Customer Name");
    }

    @Test
    void shouldSetAndGetLongLabel() {
        final var element = new DataElement();
        element.setLongLabel("Full Customer Name");
        assertThat(element.getLongLabel()).isEqualTo("Full Customer Name");
    }

    @Test
    void shouldSetAndGetDescription() {
        final var element = new DataElement();
        element.setDescription("Name of the customer");
        assertThat(element.getDescription()).isEqualTo("Name of the customer");
    }
}
