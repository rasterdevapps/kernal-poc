package com.erp.kernel.api.dto;

import java.time.Instant;

/**
 * Standard API response envelope ensuring consistent response structure
 * across all clients (web, mobile, tablet, third-party).
 *
 * <p>Wraps API responses with metadata including status, timestamp,
 * and optional message for uniform client consumption.
 *
 * @param <T>       the payload type
 * @param status    the HTTP status code
 * @param message   an optional human-readable message
 * @param data      the response payload
 * @param timestamp the response timestamp
 */
public record ApiEnvelope<T>(
        int status,
        String message,
        T data,
        Instant timestamp
) {

    /**
     * Creates a success envelope with the given data.
     *
     * @param data the response payload
     * @param <T>  the payload type
     * @return a success envelope
     */
    public static <T> ApiEnvelope<T> ok(final T data) {
        return new ApiEnvelope<>(200, "Success", data, Instant.now());
    }

    /**
     * Creates a created (201) envelope with the given data.
     *
     * @param data the response payload
     * @param <T>  the payload type
     * @return a created envelope
     */
    public static <T> ApiEnvelope<T> created(final T data) {
        return new ApiEnvelope<>(201, "Created", data, Instant.now());
    }

    /**
     * Creates an error envelope with the given status and message.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param <T>     the payload type
     * @return an error envelope
     */
    public static <T> ApiEnvelope<T> error(final int status, final String message) {
        return new ApiEnvelope<>(status, message, null, Instant.now());
    }
}
