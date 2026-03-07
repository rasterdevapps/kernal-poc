package com.erp.kernel.security.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for security entity classes.
 */
class SecurityEntityTest {

    @Test
    void shouldSetAndGetUserRoleFields() {
        final var userRole = new UserRole();
        final var now = Instant.now();
        userRole.setId(1L);
        userRole.setUserId(10L);
        userRole.setRoleId(20L);
        userRole.setAssignedAt(now);

        assertThat(userRole.getId()).isEqualTo(1L);
        assertThat(userRole.getUserId()).isEqualTo(10L);
        assertThat(userRole.getRoleId()).isEqualTo(20L);
        assertThat(userRole.getAssignedAt()).isEqualTo(now);
    }

    @Test
    void shouldImplementUserRoleEqualsAndHashCode() {
        final var ur1 = new UserRole();
        ur1.setId(1L);
        final var ur2 = new UserRole();
        ur2.setId(1L);
        final var ur3 = new UserRole();
        ur3.setId(2L);

        assertThat(ur1).isEqualTo(ur2);
        assertThat(ur1).isNotEqualTo(ur3);
        assertThat(ur1).isEqualTo(ur1);
        assertThat(ur1).isNotEqualTo(null);
        assertThat(ur1).isNotEqualTo("string");
        assertThat(ur1.hashCode()).isEqualTo(ur2.hashCode());
    }

    @Test
    void shouldSetAndGetRolePermissionFields() {
        final var rp = new RolePermission();
        rp.setId(1L);
        rp.setRoleId(10L);
        rp.setPermissionId(20L);

        assertThat(rp.getId()).isEqualTo(1L);
        assertThat(rp.getRoleId()).isEqualTo(10L);
        assertThat(rp.getPermissionId()).isEqualTo(20L);
    }

    @Test
    void shouldImplementRolePermissionEqualsAndHashCode() {
        final var rp1 = new RolePermission();
        rp1.setId(1L);
        final var rp2 = new RolePermission();
        rp2.setId(1L);
        final var rp3 = new RolePermission();
        rp3.setId(2L);

        assertThat(rp1).isEqualTo(rp2);
        assertThat(rp1).isNotEqualTo(rp3);
        assertThat(rp1).isEqualTo(rp1);
        assertThat(rp1).isNotEqualTo(null);
        assertThat(rp1).isNotEqualTo("string");
        assertThat(rp1.hashCode()).isEqualTo(rp2.hashCode());
    }

    @Test
    void shouldSetAndGetLoginAttemptFields() {
        final var attempt = new LoginAttempt();
        final var now = Instant.now();
        attempt.setId(1L);
        attempt.setUsername("user");
        attempt.setSuccess(false);
        attempt.setIpAddress("192.168.1.1");
        attempt.setUserAgent("Mozilla/5.0");
        attempt.setFailureReason("Bad password");
        attempt.setAttemptedAt(now);

        assertThat(attempt.getId()).isEqualTo(1L);
        assertThat(attempt.getUsername()).isEqualTo("user");
        assertThat(attempt.isSuccess()).isFalse();
        assertThat(attempt.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(attempt.getUserAgent()).isEqualTo("Mozilla/5.0");
        assertThat(attempt.getFailureReason()).isEqualTo("Bad password");
        assertThat(attempt.getAttemptedAt()).isEqualTo(now);
    }

    @Test
    void shouldImplementLoginAttemptEqualsAndHashCode() {
        final var la1 = new LoginAttempt();
        la1.setId(1L);
        final var la2 = new LoginAttempt();
        la2.setId(1L);
        final var la3 = new LoginAttempt();
        la3.setId(2L);

        assertThat(la1).isEqualTo(la2);
        assertThat(la1).isNotEqualTo(la3);
        assertThat(la1).isEqualTo(la1);
        assertThat(la1).isNotEqualTo(null);
        assertThat(la1).isNotEqualTo("string");
        assertThat(la1.hashCode()).isEqualTo(la2.hashCode());
    }

    @Test
    void shouldSetAndGetAuthorizationObjectFields() {
        final var ao = new AuthorizationObject();
        ao.setObjectName("S_USER_GRP");
        ao.setObjectClass("SECURITY");
        ao.setDescription("User group authorisation");

        assertThat(ao.getObjectName()).isEqualTo("S_USER_GRP");
        assertThat(ao.getObjectClass()).isEqualTo("SECURITY");
        assertThat(ao.getDescription()).isEqualTo("User group authorisation");
    }

    @Test
    void shouldSetAndGetMfaConfigurationFields() {
        final var mfa = new MfaConfiguration();
        mfa.setUserId(1L);
        mfa.setMfaType("TOTP");
        mfa.setSecretKey("SECRET123");
        mfa.setEnabled(true);
        mfa.setVerified(true);

        assertThat(mfa.getUserId()).isEqualTo(1L);
        assertThat(mfa.getMfaType()).isEqualTo("TOTP");
        assertThat(mfa.getSecretKey()).isEqualTo("SECRET123");
        assertThat(mfa.isEnabled()).isTrue();
        assertThat(mfa.isVerified()).isTrue();
    }

    @Test
    void shouldSetAndGetWebAuthnCredentialFields() {
        final var cred = new WebAuthnCredential();
        cred.setUserId(1L);
        cred.setCredentialId("cred123");
        cred.setPublicKey("pubkey");
        cred.setCredentialName("My Key");
        cred.setSignCount(42L);

        assertThat(cred.getUserId()).isEqualTo(1L);
        assertThat(cred.getCredentialId()).isEqualTo("cred123");
        assertThat(cred.getPublicKey()).isEqualTo("pubkey");
        assertThat(cred.getCredentialName()).isEqualTo("My Key");
        assertThat(cred.getSignCount()).isEqualTo(42L);
    }

    @Test
    void shouldSetAndGetUserFields() {
        final var user = new User();
        final var now = Instant.now();
        user.setUsername("jdoe");
        user.setPasswordHash("hash");
        user.setEmail("jdoe@erp.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDisplayName("John Doe");
        user.setActive(true);
        user.setLocked(false);
        user.setLdapDn("CN=jdoe");
        user.setLastLoginAt(now);
        user.setPasswordChangedAt(now);
        user.setFailedLoginCount(3);

        assertThat(user.getUsername()).isEqualTo("jdoe");
        assertThat(user.getPasswordHash()).isEqualTo("hash");
        assertThat(user.getEmail()).isEqualTo("jdoe@erp.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getDisplayName()).isEqualTo("John Doe");
        assertThat(user.isActive()).isTrue();
        assertThat(user.isLocked()).isFalse();
        assertThat(user.getLdapDn()).isEqualTo("CN=jdoe");
        assertThat(user.getLastLoginAt()).isEqualTo(now);
        assertThat(user.getPasswordChangedAt()).isEqualTo(now);
        assertThat(user.getFailedLoginCount()).isEqualTo(3);
    }

    @Test
    void shouldSetAndGetRoleFields() {
        final var role = new Role();
        role.setRoleName("ADMIN");
        role.setDescription("Administrator");
        role.setActive(true);

        assertThat(role.getRoleName()).isEqualTo("ADMIN");
        assertThat(role.getDescription()).isEqualTo("Administrator");
        assertThat(role.isActive()).isTrue();
    }

    @Test
    void shouldSetAndGetPermissionFields() {
        final var perm = new Permission();
        perm.setPermissionName("USER_READ");
        perm.setDescription("Read users");
        perm.setResource("users");
        perm.setAction("READ");

        assertThat(perm.getPermissionName()).isEqualTo("USER_READ");
        assertThat(perm.getDescription()).isEqualTo("Read users");
        assertThat(perm.getResource()).isEqualTo("users");
        assertThat(perm.getAction()).isEqualTo("READ");
    }

    @Test
    void shouldSetAndGetLoginPolicyFields() {
        final var policy = new LoginPolicy();
        policy.setPolicyName("DEFAULT");
        policy.setDescription("Default policy");
        policy.setMinPasswordLength(8);
        policy.setRequireUppercase(true);
        policy.setRequireLowercase(true);
        policy.setRequireDigit(true);
        policy.setRequireSpecialChar(true);
        policy.setPasswordExpiryDays(90);
        policy.setMaxFailedAttempts(5);
        policy.setLockoutDurationMinutes(30);
        policy.setSessionTimeoutMinutes(480);
        policy.setEnforceMfa(false);
        policy.setActive(true);

        assertThat(policy.getPolicyName()).isEqualTo("DEFAULT");
        assertThat(policy.getDescription()).isEqualTo("Default policy");
        assertThat(policy.getMinPasswordLength()).isEqualTo(8);
        assertThat(policy.isRequireUppercase()).isTrue();
        assertThat(policy.isRequireLowercase()).isTrue();
        assertThat(policy.isRequireDigit()).isTrue();
        assertThat(policy.isRequireSpecialChar()).isTrue();
        assertThat(policy.getPasswordExpiryDays()).isEqualTo(90);
        assertThat(policy.getMaxFailedAttempts()).isEqualTo(5);
        assertThat(policy.getLockoutDurationMinutes()).isEqualTo(30);
        assertThat(policy.getSessionTimeoutMinutes()).isEqualTo(480);
        assertThat(policy.isEnforceMfa()).isFalse();
        assertThat(policy.isActive()).isTrue();
    }
}
