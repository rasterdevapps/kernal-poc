package com.erp.kernel.security.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for security DTO record classes.
 */
class SecurityDtoTest {

    @Test
    void shouldCreateUserDto() {
        final var now = Instant.now();
        final var dto = new UserDto(1L, "jdoe", "jdoe@erp.com", "John", "Doe",
                "John Doe", true, false, "CN=jdoe", now, now, 0, now, now);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.username()).isEqualTo("jdoe");
        assertThat(dto.email()).isEqualTo("jdoe@erp.com");
    }

    @Test
    void shouldCreateRoleDto() {
        final var now = Instant.now();
        final var dto = new RoleDto(1L, "ADMIN", "Administrator", true, now, now);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.roleName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldCreatePermissionDto() {
        final var now = Instant.now();
        final var dto = new PermissionDto(1L, "USER_READ", "Read users", "users", "READ", now, now);

        assertThat(dto.permissionName()).isEqualTo("USER_READ");
        assertThat(dto.resource()).isEqualTo("users");
    }

    @Test
    void shouldCreateLoginPolicyDto() {
        final var now = Instant.now();
        final var dto = new LoginPolicyDto(1L, "DEFAULT", "desc", 8, true, true, true, true,
                90, 5, 30, 480, false, true, now, now);

        assertThat(dto.policyName()).isEqualTo("DEFAULT");
        assertThat(dto.minPasswordLength()).isEqualTo(8);
    }

    @Test
    void shouldCreateMfaSetupResponse() {
        final var resp = new MfaSetupResponse("SECRET", "otpauth://totp/...");

        assertThat(resp.secretKey()).isEqualTo("SECRET");
        assertThat(resp.provisioningUri()).startsWith("otpauth://");
    }

    @Test
    void shouldCreateAuthenticationRequest() {
        final var req = new AuthenticationRequest("user", "pass", "123456");

        assertThat(req.username()).isEqualTo("user");
        assertThat(req.password()).isEqualTo("pass");
        assertThat(req.totpCode()).isEqualTo("123456");
    }

    @Test
    void shouldCreateAuthenticationResponse() {
        final var resp = new AuthenticationResponse(true, "user", "OK", false);

        assertThat(resp.authenticated()).isTrue();
        assertThat(resp.username()).isEqualTo("user");
        assertThat(resp.mfaRequired()).isFalse();
    }

    @Test
    void shouldCreateCreateUserRequest() {
        final var req = new CreateUserRequest("jdoe", "pass", "jdoe@erp.com",
                "John", "Doe", "John Doe", "CN=jdoe");

        assertThat(req.username()).isEqualTo("jdoe");
        assertThat(req.password()).isEqualTo("pass");
    }

    @Test
    void shouldCreateCreateRoleRequest() {
        final var req = new CreateRoleRequest("ADMIN", "Administrator");

        assertThat(req.roleName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldCreateCreateLoginPolicyRequest() {
        final var req = new CreateLoginPolicyRequest("DEFAULT", "desc", 8, true, true,
                true, true, 90, 5, 30, 480, false);

        assertThat(req.policyName()).isEqualTo("DEFAULT");
        assertThat(req.minPasswordLength()).isEqualTo(8);
    }
}
