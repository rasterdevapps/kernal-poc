package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.Domain;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link DataElementMapper}.
 */
class DataElementMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var domain = createDomain(1L, "CHAR_30");
        final var entity = createDataElementEntity(2L, "CUSTOMER_NAME", domain, "CustNm", "Customer Nm", "Customer Name", "Description");
        final var dto = DataElementMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.elementName()).isEqualTo("CUSTOMER_NAME");
        assertThat(dto.domainId()).isEqualTo(1L);
        assertThat(dto.domainName()).isEqualTo("CHAR_30");
        assertThat(dto.shortLabel()).isEqualTo("CustNm");
        assertThat(dto.mediumLabel()).isEqualTo("Customer Nm");
        assertThat(dto.longLabel()).isEqualTo("Customer Name");
        assertThat(dto.description()).isEqualTo("Description");
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var domain = createDomain(1L, "CHAR_30");
        final var request = new CreateDataElementRequest("CUST_NAME", 1L, "CN", "Cust Name", "Customer Name", "Desc");
        final var entity = DataElementMapper.toEntity(request, domain);

        assertThat(entity.getElementName()).isEqualTo("CUST_NAME");
        assertThat(entity.getDomain()).isEqualTo(domain);
        assertThat(entity.getShortLabel()).isEqualTo("CN");
        assertThat(entity.getMediumLabel()).isEqualTo("Cust Name");
        assertThat(entity.getLongLabel()).isEqualTo("Customer Name");
        assertThat(entity.getDescription()).isEqualTo("Desc");
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var oldDomain = createDomain(1L, "OLD_DOMAIN");
        final var newDomain = createDomain(2L, "NEW_DOMAIN");
        final var entity = createDataElementEntity(1L, "OLD_NAME", oldDomain, "O", "Old", "Old Name", "Old desc");
        final var request = new CreateDataElementRequest("NEW_NAME", 2L, "N", "New", "New Name", "New desc");
        DataElementMapper.updateEntity(entity, request, newDomain);

        assertThat(entity.getElementName()).isEqualTo("NEW_NAME");
        assertThat(entity.getDomain()).isEqualTo(newDomain);
        assertThat(entity.getShortLabel()).isEqualTo("N");
    }

    @Test
    void shouldThrowNullPointerException_whenToDtoEntityIsNull() {
        assertThatThrownBy(() -> DataElementMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityRequestIsNull() {
        assertThatThrownBy(() -> DataElementMapper.toEntity(null, new Domain()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToEntityDomainIsNull() {
        final var request = new CreateDataElementRequest("NAME", 1L, null, null, null, null);
        assertThatThrownBy(() -> DataElementMapper.toEntity(request, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateDataElementRequest("NAME", 1L, null, null, null, null);
        assertThatThrownBy(() -> DataElementMapper.updateEntity(null, request, new Domain()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        assertThatThrownBy(() -> DataElementMapper.updateEntity(new DataElement(), null, new Domain()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateDomainIsNull() {
        final var request = new CreateDataElementRequest("NAME", 1L, null, null, null, null);
        assertThatThrownBy(() -> DataElementMapper.updateEntity(new DataElement(), request, null))
                .isInstanceOf(NullPointerException.class);
    }

    private Domain createDomain(final Long id, final String name) {
        final var domain = new Domain();
        domain.setId(id);
        domain.setDomainName(name);
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());
        return domain;
    }

    private DataElement createDataElementEntity(final Long id, final String name, final Domain domain,
                                                 final String shortLabel, final String mediumLabel,
                                                 final String longLabel, final String desc) {
        final var element = new DataElement();
        element.setId(id);
        element.setElementName(name);
        element.setDomain(domain);
        element.setShortLabel(shortLabel);
        element.setMediumLabel(mediumLabel);
        element.setLongLabel(longLabel);
        element.setDescription(desc);
        element.setCreatedAt(Instant.now());
        element.setUpdatedAt(Instant.now());
        return element;
    }
}
