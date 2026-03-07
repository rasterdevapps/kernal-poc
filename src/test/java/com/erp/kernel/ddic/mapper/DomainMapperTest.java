package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.entity.Domain;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link DomainMapper}.
 */
class DomainMapperTest {

    @Test
    void shouldConvertEntityToDto() {
        final var entity = createDomainEntity(1L, "CHAR_30", "CHAR", 30, 0, "Character domain");
        final var dto = DomainMapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.domainName()).isEqualTo("CHAR_30");
        assertThat(dto.dataType()).isEqualTo("CHAR");
        assertThat(dto.maxLength()).isEqualTo(30);
        assertThat(dto.decimalPlaces()).isEqualTo(0);
        assertThat(dto.description()).isEqualTo("Character domain");
        assertThat(dto.createdAt()).isNotNull();
        assertThat(dto.updatedAt()).isNotNull();
    }

    @Test
    void shouldConvertRequestToEntity() {
        final var request = new CreateDomainRequest("DEC_10_2", "DEC", 10, 2, "Decimal domain");
        final var entity = DomainMapper.toEntity(request);

        assertThat(entity.getDomainName()).isEqualTo("DEC_10_2");
        assertThat(entity.getDataType()).isEqualTo("DEC");
        assertThat(entity.getMaxLength()).isEqualTo(10);
        assertThat(entity.getDecimalPlaces()).isEqualTo(2);
        assertThat(entity.getDescription()).isEqualTo("Decimal domain");
        assertThat(entity.getId()).isNull();
    }

    @Test
    void shouldUpdateExistingEntity() {
        final var entity = createDomainEntity(1L, "OLD_NAME", "CHAR", 10, 0, "Old desc");
        final var request = new CreateDomainRequest("NEW_NAME", "INT", 5, null, "New desc");
        DomainMapper.updateEntity(entity, request);

        assertThat(entity.getDomainName()).isEqualTo("NEW_NAME");
        assertThat(entity.getDataType()).isEqualTo("INT");
        assertThat(entity.getMaxLength()).isEqualTo(5);
        assertThat(entity.getDecimalPlaces()).isNull();
        assertThat(entity.getDescription()).isEqualTo("New desc");
    }

    @Test
    void shouldThrowNullPointerException_whenEntityIsNull() {
        assertThatThrownBy(() -> DomainMapper.toDto(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNull() {
        assertThatThrownBy(() -> DomainMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateEntityIsNull() {
        final var request = new CreateDomainRequest("NAME", "CHAR", 10, 0, "desc");
        assertThatThrownBy(() -> DomainMapper.updateEntity(null, request))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenUpdateRequestIsNull() {
        final var entity = new Domain();
        assertThatThrownBy(() -> DomainMapper.updateEntity(entity, null))
                .isInstanceOf(NullPointerException.class);
    }

    private Domain createDomainEntity(final Long id, final String name, final String dataType,
                                       final Integer maxLength, final Integer decimals,
                                       final String description) {
        final var domain = new Domain();
        domain.setId(id);
        domain.setDomainName(name);
        domain.setDataType(dataType);
        domain.setMaxLength(maxLength);
        domain.setDecimalPlaces(decimals);
        domain.setDescription(description);
        domain.setCreatedAt(Instant.now());
        domain.setUpdatedAt(Instant.now());
        return domain;
    }
}
