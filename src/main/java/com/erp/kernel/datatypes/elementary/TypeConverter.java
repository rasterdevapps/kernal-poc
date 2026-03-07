package com.erp.kernel.datatypes.elementary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Converts string values to and from their typed Java representations
 * based on the {@link ElementaryDataType}.
 *
 * <p>This utility enables serialisation and deserialisation of ABAP-style
 * data values between their string transport format and Java object types.
 */
public final class TypeConverter {

    private static final DateTimeFormatter DATS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    private TypeConverter() {
    }

    /**
     * Converts a string value to its corresponding Java object based on the data type.
     *
     * @param type  the elementary data type
     * @param value the string value to convert
     * @return the typed Java object, or {@code null} if the input value is {@code null}
     * @throws IllegalArgumentException if the value cannot be converted
     */
    public static Object toJavaObject(final ElementaryDataType type, final String value) {
        Objects.requireNonNull(type, "type must not be null");
        if (value == null) {
            return null;
        }
        return switch (type) {
            case CHAR, NUMC, STRING, CLNT, LANG, UNIT, CUKY, ACCP -> value;
            case DATS -> LocalDate.parse(value, DATS_FORMATTER);
            case TIMS -> LocalTime.parse(value, TIMS_FORMATTER);
            case INT1 -> Short.parseShort(value);
            case INT2 -> Short.parseShort(value);
            case INT4 -> Integer.parseInt(value);
            case INT8 -> Long.parseLong(value);
            case DEC, CURR, QUAN -> new BigDecimal(value);
            case FLTP -> Double.parseDouble(value);
            case RAW, RAWSTRING -> value.getBytes();
        };
    }

    /**
     * Converts a typed Java object to its string representation based on the data type.
     *
     * @param type  the elementary data type
     * @param value the Java object to convert
     * @return the string representation, or {@code null} if the input value is {@code null}
     */
    public static String toStringValue(final ElementaryDataType type, final Object value) {
        Objects.requireNonNull(type, "type must not be null");
        if (value == null) {
            return null;
        }
        return switch (type) {
            case DATS -> DATS_FORMATTER.format((LocalDate) value);
            case TIMS -> TIMS_FORMATTER.format((LocalTime) value);
            case RAW, RAWSTRING -> new String((byte[]) value);
            default -> value.toString();
        };
    }
}
