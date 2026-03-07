package com.erp.kernel.datatypes.elementary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Validates values against {@link ElementaryDataType} constraints.
 *
 * <p>Enforces type-specific rules such as length limits, numeric-only content,
 * date/time format compliance, and decimal precision.
 */
public final class TypeValidator {

    private static final DateTimeFormatter DATS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    private static final String NUMC_PATTERN = "^\\d*$";
    private static final String ACCP_PATTERN = "^\\d{6}$";
    private static final int INT1_MAX = 255;
    private static final int INT1_MIN = 0;
    private static final int CLNT_LENGTH = 3;
    private static final int LANG_LENGTH = 1;
    private static final int UNIT_MAX_LENGTH = 3;
    private static final int CUKY_MAX_LENGTH = 5;
    private static final int ACCP_LENGTH = 6;

    private TypeValidator() {
    }

    /**
     * Validates a string value against the given elementary data type and constraints.
     *
     * @param type          the elementary data type
     * @param value         the string value to validate
     * @param maxLength     the maximum length (may be {@code null})
     * @param decimalPlaces the decimal places (may be {@code null})
     * @return {@code true} if the value is valid for the given type
     */
    public static boolean isValid(final ElementaryDataType type,
                                  final String value,
                                  final Integer maxLength,
                                  final Integer decimalPlaces) {
        Objects.requireNonNull(type, "type must not be null");
        if (value == null || value.isEmpty()) {
            return true;
        }
        return switch (type) {
            case CHAR -> validateChar(value, maxLength);
            case NUMC -> validateNumc(value, maxLength);
            case DATS -> validateDats(value);
            case TIMS -> validateTims(value);
            case INT1 -> validateInt1(value);
            case INT2 -> validateInt2(value);
            case INT4 -> validateInt4(value);
            case INT8 -> validateInt8(value);
            case DEC, CURR, QUAN -> validateDecimal(value, maxLength, decimalPlaces);
            case FLTP -> validateFltp(value);
            case STRING, RAWSTRING, RAW -> validateString(value, maxLength);
            case CLNT -> validateFixedLength(value, CLNT_LENGTH);
            case LANG -> validateFixedLength(value, LANG_LENGTH);
            case UNIT -> validateMaxLength(value, UNIT_MAX_LENGTH);
            case CUKY -> validateMaxLength(value, CUKY_MAX_LENGTH);
            case ACCP -> validateAccp(value);
        };
    }

    private static boolean validateChar(final String value, final Integer maxLength) {
        if (maxLength != null && value.length() > maxLength) {
            return false;
        }
        return true;
    }

    private static boolean validateNumc(final String value, final Integer maxLength) {
        if (!value.matches(NUMC_PATTERN)) {
            return false;
        }
        if (maxLength != null && value.length() > maxLength) {
            return false;
        }
        return true;
    }

    private static boolean validateDats(final String value) {
        try {
            LocalDate.parse(value, DATS_FORMATTER);
            return true;
        } catch (final DateTimeParseException e) {
            return false;
        }
    }

    private static boolean validateTims(final String value) {
        try {
            LocalTime.parse(value, TIMS_FORMATTER);
            return true;
        } catch (final DateTimeParseException e) {
            return false;
        }
    }

    private static boolean validateInt1(final String value) {
        try {
            final var parsed = Integer.parseInt(value);
            return parsed >= INT1_MIN && parsed <= INT1_MAX;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateInt2(final String value) {
        try {
            Short.parseShort(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateInt4(final String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateInt8(final String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateDecimal(final String value,
                                           final Integer maxLength,
                                           final Integer decimalPlaces) {
        try {
            final var decimal = new BigDecimal(value);
            if (maxLength != null) {
                final var precision = decimal.precision();
                if (precision > maxLength) {
                    return false;
                }
            }
            if (decimalPlaces != null && decimal.scale() > decimalPlaces) {
                return false;
            }
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateFltp(final String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateString(final String value, final Integer maxLength) {
        if (maxLength != null && value.length() > maxLength) {
            return false;
        }
        return true;
    }

    private static boolean validateFixedLength(final String value, final int expectedLength) {
        return value.length() == expectedLength;
    }

    private static boolean validateMaxLength(final String value, final int maxLength) {
        return value.length() <= maxLength;
    }

    private static boolean validateAccp(final String value) {
        if (value.length() != ACCP_LENGTH) {
            return false;
        }
        return value.matches(ACCP_PATTERN);
    }
}
