package com.erp.kernel.datatypes.elementary;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link TypeConverter}.
 */
class TypeConverterTest {

    @Test
    void shouldReturnNull_whenToJavaObjectValueIsNull() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.CHAR, null)).isNull();
    }

    @Test
    void shouldReturnNull_whenToStringValueIsNull() {
        assertThat(TypeConverter.toStringValue(ElementaryDataType.CHAR, null)).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenToJavaObjectTypeIsNull() {
        assertThatThrownBy(() -> TypeConverter.toJavaObject(null, "test"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenToStringTypeIsNull() {
        assertThatThrownBy(() -> TypeConverter.toStringValue(null, "test"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldConvertCharToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.CHAR, "hello")).isEqualTo("hello");
    }

    @Test
    void shouldConvertNumcToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.NUMC, "00123")).isEqualTo("00123");
    }

    @Test
    void shouldConvertStringType() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.STRING, "text")).isEqualTo("text");
    }

    @Test
    void shouldConvertClntToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.CLNT, "001")).isEqualTo("001");
    }

    @Test
    void shouldConvertLangToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.LANG, "E")).isEqualTo("E");
    }

    @Test
    void shouldConvertUnitToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.UNIT, "KG")).isEqualTo("KG");
    }

    @Test
    void shouldConvertCukyToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.CUKY, "USD")).isEqualTo("USD");
    }

    @Test
    void shouldConvertAccpToString() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.ACCP, "202603")).isEqualTo("202603");
    }

    @Test
    void shouldConvertDatsToLocalDate() {
        final var result = TypeConverter.toJavaObject(ElementaryDataType.DATS, "20260307");
        assertThat(result).isEqualTo(LocalDate.of(2026, 3, 7));
    }

    @Test
    void shouldConvertTimsToLocalTime() {
        final var result = TypeConverter.toJavaObject(ElementaryDataType.TIMS, "143022");
        assertThat(result).isEqualTo(LocalTime.of(14, 30, 22));
    }

    @Test
    void shouldConvertInt1ToShort() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.INT1, "100"))
                .isEqualTo((short) 100);
    }

    @Test
    void shouldConvertInt2ToShort() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.INT2, "30000"))
                .isEqualTo((short) 30000);
    }

    @Test
    void shouldConvertInt4ToInteger() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.INT4, "42")).isEqualTo(42);
    }

    @Test
    void shouldConvertInt8ToLong() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.INT8, "9999999999"))
                .isEqualTo(9999999999L);
    }

    @Test
    void shouldConvertDecToBigDecimal() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.DEC, "123.45"))
                .isEqualTo(new BigDecimal("123.45"));
    }

    @Test
    void shouldConvertCurrToBigDecimal() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.CURR, "1000.50"))
                .isEqualTo(new BigDecimal("1000.50"));
    }

    @Test
    void shouldConvertQuanToBigDecimal() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.QUAN, "50.5"))
                .isEqualTo(new BigDecimal("50.5"));
    }

    @Test
    void shouldConvertFltpToDouble() {
        assertThat(TypeConverter.toJavaObject(ElementaryDataType.FLTP, "3.14"))
                .isEqualTo(3.14);
    }

    @Test
    void shouldConvertRawToByteArray() {
        final var result = TypeConverter.toJavaObject(ElementaryDataType.RAW, "data");
        assertThat(result).isInstanceOf(byte[].class);
        assertThat(new String((byte[]) result)).isEqualTo("data");
    }

    @Test
    void shouldConvertRawstringToByteArray() {
        final var result = TypeConverter.toJavaObject(ElementaryDataType.RAWSTRING, "bytes");
        assertThat(result).isInstanceOf(byte[].class);
    }

    // toStringValue tests
    @Test
    void shouldConvertLocalDateToString() {
        final var result = TypeConverter.toStringValue(
                ElementaryDataType.DATS, LocalDate.of(2026, 3, 7));
        assertThat(result).isEqualTo("20260307");
    }

    @Test
    void shouldConvertLocalTimeToString() {
        final var result = TypeConverter.toStringValue(
                ElementaryDataType.TIMS, LocalTime.of(14, 30, 22));
        assertThat(result).isEqualTo("143022");
    }

    @Test
    void shouldConvertByteArrayToString() {
        final var result = TypeConverter.toStringValue(
                ElementaryDataType.RAW, "data".getBytes());
        assertThat(result).isEqualTo("data");
    }

    @Test
    void shouldConvertRawstringByteArrayToString() {
        final var result = TypeConverter.toStringValue(
                ElementaryDataType.RAWSTRING, "bytes".getBytes());
        assertThat(result).isEqualTo("bytes");
    }

    @Test
    void shouldConvertIntegerToString() {
        final var result = TypeConverter.toStringValue(ElementaryDataType.INT4, 42);
        assertThat(result).isEqualTo("42");
    }

    @Test
    void shouldConvertBigDecimalToString() {
        final var result = TypeConverter.toStringValue(
                ElementaryDataType.DEC, new BigDecimal("123.45"));
        assertThat(result).isEqualTo("123.45");
    }

    @Test
    void shouldConvertStringValueForChar() {
        final var result = TypeConverter.toStringValue(ElementaryDataType.CHAR, "hello");
        assertThat(result).isEqualTo("hello");
    }
}
