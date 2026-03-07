package com.erp.kernel.ddic.exception;

/**
 * Standard error response returned by REST endpoints when an exception occurs.
 *
 * @param message a human-readable description of the error
 */
public record ErrorResponse(String message) {
}
