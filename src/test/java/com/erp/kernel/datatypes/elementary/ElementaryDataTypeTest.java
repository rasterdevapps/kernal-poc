package com.erp.kernel.datatypes.elementary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ElementaryDataType}.
 */
class ElementaryDataTypeTest {

    @ParameterizedTest
    @EnumSource(ElementaryDataType.class)
    void shouldHaveNonNullJavaType(final ElementaryDataType type) {
        assertThat(type.getJavaType()).isNotNull().isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(ElementaryDataType.class)
    void shouldHaveNonNullPostgresType(final ElementaryDataType type) {
        assertThat(type.getPostgresType()).isNotNull().isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(ElementaryDataType.class)
    void shouldHaveNonNullDescription(final ElementaryDataType type) {
        assertThat(type.getDescription()).isNotNull().isNotBlank();
    }

    @Test
    void shouldResolvePostgresType_whenLengthAndDecimalsProvided() {
        final var result = ElementaryDataType.DEC.resolvePostgresType(10, 2);
        assertThat(result).isEqualTo("NUMERIC(10,2)");
    }

    @Test
    void shouldResolvePostgresType_whenLengthProvidedWithDefaultDecimals() {
        final var result = ElementaryDataType.CURR.resolvePostgresType(15, null);
        assertThat(result).isEqualTo("NUMERIC(15,0)");
    }

    @Test
    void shouldResolvePostgresType_whenLengthOnlySupported() {
        final var result = ElementaryDataType.CHAR.resolvePostgresType(30, null);
        assertThat(result).isEqualTo("VARCHAR(30)");
    }

    @Test
    void shouldResolvePostgresType_whenNoLengthProvided() {
        final var result = ElementaryDataType.CHAR.resolvePostgresType(null, null);
        assertThat(result).isEqualTo("VARCHAR");
    }

    @Test
    void shouldResolvePostgresType_whenTypeDoesNotSupportLength() {
        final var result = ElementaryDataType.INT4.resolvePostgresType(10, null);
        assertThat(result).isEqualTo("INTEGER");
    }

    @Test
    void shouldResolvePostgresType_whenDecimalTypeWithNoLength() {
        final var result = ElementaryDataType.DEC.resolvePostgresType(null, 2);
        assertThat(result).isEqualTo("NUMERIC");
    }

    @Test
    void shouldMapCharToString() {
        assertThat(ElementaryDataType.CHAR.getJavaType()).isEqualTo("java.lang.String");
        assertThat(ElementaryDataType.CHAR.isSupportsLength()).isTrue();
        assertThat(ElementaryDataType.CHAR.isSupportsDecimals()).isFalse();
    }

    @Test
    void shouldMapNumcToString() {
        assertThat(ElementaryDataType.NUMC.getJavaType()).isEqualTo("java.lang.String");
    }

    @Test
    void shouldMapDatsToLocalDate() {
        assertThat(ElementaryDataType.DATS.getJavaType()).isEqualTo("java.time.LocalDate");
        assertThat(ElementaryDataType.DATS.isSupportsLength()).isFalse();
    }

    @Test
    void shouldMapTimsToLocalTime() {
        assertThat(ElementaryDataType.TIMS.getJavaType()).isEqualTo("java.time.LocalTime");
    }

    @Test
    void shouldMapIntTypes() {
        assertThat(ElementaryDataType.INT1.getJavaType()).isEqualTo("java.lang.Short");
        assertThat(ElementaryDataType.INT2.getJavaType()).isEqualTo("java.lang.Short");
        assertThat(ElementaryDataType.INT4.getJavaType()).isEqualTo("java.lang.Integer");
        assertThat(ElementaryDataType.INT8.getJavaType()).isEqualTo("java.lang.Long");
    }

    @Test
    void shouldMapDecimalTypes() {
        assertThat(ElementaryDataType.DEC.isSupportsLength()).isTrue();
        assertThat(ElementaryDataType.DEC.isSupportsDecimals()).isTrue();
        assertThat(ElementaryDataType.CURR.isSupportsDecimals()).isTrue();
        assertThat(ElementaryDataType.QUAN.isSupportsDecimals()).isTrue();
    }

    @Test
    void shouldMapFltpToDouble() {
        assertThat(ElementaryDataType.FLTP.getJavaType()).isEqualTo("java.lang.Double");
    }

    @Test
    void shouldMapStringToText() {
        assertThat(ElementaryDataType.STRING.getPostgresType()).isEqualTo("TEXT");
    }

    @Test
    void shouldMapRawTypes() {
        assertThat(ElementaryDataType.RAW.getJavaType()).isEqualTo("byte[]");
        assertThat(ElementaryDataType.RAW.isSupportsLength()).isTrue();
        assertThat(ElementaryDataType.RAWSTRING.getJavaType()).isEqualTo("byte[]");
    }

    @Test
    void shouldMapSpecialTypes() {
        assertThat(ElementaryDataType.CLNT.getPostgresType()).isEqualTo("VARCHAR(3)");
        assertThat(ElementaryDataType.LANG.getPostgresType()).isEqualTo("VARCHAR(1)");
        assertThat(ElementaryDataType.UNIT.getPostgresType()).isEqualTo("VARCHAR(3)");
        assertThat(ElementaryDataType.CUKY.getPostgresType()).isEqualTo("VARCHAR(5)");
        assertThat(ElementaryDataType.ACCP.getPostgresType()).isEqualTo("VARCHAR(6)");
    }

    @Test
    void shouldContainAllExpectedTypes() {
        assertThat(ElementaryDataType.values()).hasSize(20);
    }

    @Test
    void shouldResolveQuanPostgresType_whenLengthAndDecimalsProvided() {
        final var result = ElementaryDataType.QUAN.resolvePostgresType(13, 3);
        assertThat(result).isEqualTo("NUMERIC(13,3)");
    }

    @Test
    void shouldResolveNumcPostgresType_whenLengthProvided() {
        final var result = ElementaryDataType.NUMC.resolvePostgresType(10, null);
        assertThat(result).isEqualTo("VARCHAR(10)");
    }

    @Test
    void shouldResolveRawPostgresType_whenLengthProvided() {
        final var result = ElementaryDataType.RAW.resolvePostgresType(256, null);
        assertThat(result).isEqualTo("BYTEA(256)");
    }
}
