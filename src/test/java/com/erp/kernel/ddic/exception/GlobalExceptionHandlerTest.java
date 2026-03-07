package com.erp.kernel.ddic.exception;

import com.erp.kernel.security.exception.AccountLockedException;
import com.erp.kernel.security.exception.AuthenticationException;
import com.erp.kernel.security.exception.AuthorizationException;
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

    @Test
    void shouldReturnUnauthorized_whenAuthenticationExceptionThrown() {
        final var ex = new AuthenticationException("Invalid credentials");

        final var response = handler.handleAuthentication(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Invalid credentials");
    }

    @Test
    void shouldReturnForbidden_whenAuthorizationExceptionThrown() {
        final var ex = new AuthorizationException("Access denied");

        final var response = handler.handleAuthorization(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Access denied");
    }

    @Test
    void shouldReturnLocked_whenAccountLockedExceptionThrown() {
        final var ex = new AccountLockedException("Account is locked");

        final var response = handler.handleAccountLocked(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Account is locked");
    }
}
