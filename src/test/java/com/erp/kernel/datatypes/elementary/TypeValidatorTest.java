package com.erp.kernel.datatypes.elementary;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link TypeValidator}.
 */
class TypeValidatorTest {

    @Test
    void shouldReturnTrue_whenValueIsNull() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CHAR, null, 10, null)).isTrue();
    }

    @Test
    void shouldReturnTrue_whenValueIsEmpty() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CHAR, "", 10, null)).isTrue();
    }

    @Test
    void shouldThrowNullPointerException_whenTypeIsNull() {
        assertThatThrownBy(() -> TypeValidator.isValid(null, "test", null, null))
                .isInstanceOf(NullPointerException.class);
    }

    // CHAR tests
    @Test
    void shouldValidateChar_whenWithinLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CHAR, "hello", 10, null)).isTrue();
    }

    @Test
    void shouldInvalidateChar_whenExceedsLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CHAR, "hello world", 5, null)).isFalse();
    }

    @Test
    void shouldValidateChar_whenNoLengthConstraint() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CHAR, "any length string", null, null)).isTrue();
    }

    // NUMC tests
    @Test
    void shouldValidateNumc_whenAllDigits() {
        assertThat(TypeValidator.isValid(ElementaryDataType.NUMC, "001234", 10, null)).isTrue();
    }

    @Test
    void shouldInvalidateNumc_whenContainsNonDigits() {
        assertThat(TypeValidator.isValid(ElementaryDataType.NUMC, "123abc", 10, null)).isFalse();
    }

    @Test
    void shouldInvalidateNumc_whenExceedsLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.NUMC, "12345", 3, null)).isFalse();
    }

    @Test
    void shouldValidateNumc_whenNoLengthConstraint() {
        assertThat(TypeValidator.isValid(ElementaryDataType.NUMC, "123456789", null, null)).isTrue();
    }

    // DATS tests
    @Test
    void shouldValidateDats_whenValidDate() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DATS, "20260307", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateDats_whenInvalidDate() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DATS, "99999999", null, null)).isFalse();
    }

    @Test
    void shouldInvalidateDats_whenWrongFormat() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DATS, "2026-03-07", null, null)).isFalse();
    }

    // TIMS tests
    @Test
    void shouldValidateTims_whenValidTime() {
        assertThat(TypeValidator.isValid(ElementaryDataType.TIMS, "143022", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateTims_whenInvalidTime() {
        assertThat(TypeValidator.isValid(ElementaryDataType.TIMS, "250000", null, null)).isFalse();
    }

    // INT1 tests
    @Test
    void shouldValidateInt1_whenInRange() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT1, "255", null, null)).isTrue();
        assertThat(TypeValidator.isValid(ElementaryDataType.INT1, "0", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateInt1_whenOutOfRange() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT1, "256", null, null)).isFalse();
        assertThat(TypeValidator.isValid(ElementaryDataType.INT1, "-1", null, null)).isFalse();
    }

    @Test
    void shouldInvalidateInt1_whenNotNumeric() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT1, "abc", null, null)).isFalse();
    }

    // INT2 tests
    @Test
    void shouldValidateInt2_whenInRange() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT2, "32767", null, null)).isTrue();
        assertThat(TypeValidator.isValid(ElementaryDataType.INT2, "-32768", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateInt2_whenOutOfRange() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT2, "32768", null, null)).isFalse();
    }

    // INT4 tests
    @Test
    void shouldValidateInt4_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT4, "2147483647", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateInt4_whenInvalid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT4, "not_a_number", null, null)).isFalse();
    }

    // INT8 tests
    @Test
    void shouldValidateInt8_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT8, "9223372036854775807", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateInt8_whenInvalid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.INT8, "abc", null, null)).isFalse();
    }

    // DEC tests
    @Test
    void shouldValidateDec_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DEC, "123.45", 5, 2)).isTrue();
    }

    @Test
    void shouldInvalidateDec_whenPrecisionExceeded() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DEC, "123456", 5, 2)).isFalse();
    }

    @Test
    void shouldInvalidateDec_whenScaleExceeded() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DEC, "1.123", 5, 2)).isFalse();
    }

    @Test
    void shouldInvalidateDec_whenNotNumeric() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DEC, "abc", 10, 2)).isFalse();
    }

    @Test
    void shouldValidateDec_whenNoConstraints() {
        assertThat(TypeValidator.isValid(ElementaryDataType.DEC, "99999.99999", null, null)).isTrue();
    }

    // CURR and QUAN delegated to same decimal validator
    @Test
    void shouldValidateCurr_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CURR, "1000.50", 10, 2)).isTrue();
    }

    @Test
    void shouldValidateQuan_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.QUAN, "50.5", 5, 1)).isTrue();
    }

    // FLTP tests
    @Test
    void shouldValidateFltp_whenValid() {
        assertThat(TypeValidator.isValid(ElementaryDataType.FLTP, "3.14159", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateFltp_whenNotNumeric() {
        assertThat(TypeValidator.isValid(ElementaryDataType.FLTP, "pi", null, null)).isFalse();
    }

    // STRING tests
    @Test
    void shouldValidateString_whenWithinLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.STRING, "test", 100, null)).isTrue();
    }

    @Test
    void shouldValidateString_whenNoLengthConstraint() {
        assertThat(TypeValidator.isValid(ElementaryDataType.STRING, "any length", null, null)).isTrue();
    }

    // RAWSTRING tests
    @Test
    void shouldValidateRawstring_whenWithinLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.RAWSTRING, "data", 100, null)).isTrue();
    }

    // RAW tests
    @Test
    void shouldValidateRaw_whenWithinLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.RAW, "abc", 10, null)).isTrue();
    }

    @Test
    void shouldInvalidateRaw_whenExceedsLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.RAW, "abcdefgh", 3, null)).isFalse();
    }

    // CLNT tests
    @Test
    void shouldValidateClnt_whenExactLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CLNT, "001", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateClnt_whenWrongLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CLNT, "01", null, null)).isFalse();
        assertThat(TypeValidator.isValid(ElementaryDataType.CLNT, "0001", null, null)).isFalse();
    }

    // LANG tests
    @Test
    void shouldValidateLang_whenExactLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.LANG, "E", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateLang_whenWrongLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.LANG, "EN", null, null)).isFalse();
    }

    // UNIT tests
    @Test
    void shouldValidateUnit_whenWithinMaxLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.UNIT, "KG", null, null)).isTrue();
        assertThat(TypeValidator.isValid(ElementaryDataType.UNIT, "PCS", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateUnit_whenExceedsMaxLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.UNIT, "KILO", null, null)).isFalse();
    }

    // CUKY tests
    @Test
    void shouldValidateCuky_whenWithinMaxLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CUKY, "USD", null, null)).isTrue();
        assertThat(TypeValidator.isValid(ElementaryDataType.CUKY, "EUR", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateCuky_whenExceedsMaxLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.CUKY, "DOLLAR", null, null)).isFalse();
    }

    // ACCP tests
    @Test
    void shouldValidateAccp_whenValidFormat() {
        assertThat(TypeValidator.isValid(ElementaryDataType.ACCP, "202603", null, null)).isTrue();
    }

    @Test
    void shouldInvalidateAccp_whenWrongLength() {
        assertThat(TypeValidator.isValid(ElementaryDataType.ACCP, "20260", null, null)).isFalse();
    }

    @Test
    void shouldInvalidateAccp_whenNonNumeric() {
        assertThat(TypeValidator.isValid(ElementaryDataType.ACCP, "2026AB", null, null)).isFalse();
    }
}
