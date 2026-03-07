package com.erp.kernel.ddic.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GlobalExceptionHandler}.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturnNotFound_whenEntityNotFoundExceptionThrown() {
        final var ex = new EntityNotFoundException("Domain not found");

        final var response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Domain not found");
    }

    @Test
    void shouldReturnConflict_whenDuplicateEntityExceptionThrown() {
        final var ex = new DuplicateEntityException("Domain already exists");

        final var response = handler.handleDuplicate(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Domain already exists");
    }

    @Test
    void shouldReturnBadRequest_whenMethodArgumentNotValidExceptionThrown() {
        final var bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "name", "must not be blank"));
        final var ex = new MethodArgumentNotValidException(null, bindingResult);

        final var response = handler.handleMethodArgumentNotValid(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).contains("name: must not be blank");
    }

    @Test
    void shouldReturnBadRequest_whenValidationExceptionThrown() {
        final var ex = new ValidationException("Invalid domain name");

        final var response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Invalid domain name");
    }
}
