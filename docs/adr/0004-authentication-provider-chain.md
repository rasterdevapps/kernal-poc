# ADR-0004: Extensible Authentication Provider Chain

| Field       | Value                                               |
|-------------|-----------------------------------------------------|
| **Status**  | Accepted                                            |
| **Date**    | 2025-02-15                                          |
| **Phase**   | Phase 4 — Security, Authentication & User Management|
| **Milestone** | 4.7 — Extensible Authentication Framework          |

---

## Context

Enterprise ERP systems must support diverse authentication mechanisms depending on
the deployment environment:

- **Small organisations** use local database credentials.
- **Enterprises** integrate with LDAP/Active Directory.
- **Cloud deployments** require SAML 2.0 or OpenID Connect for SSO.
- **High-security environments** mandate FIDO2/WebAuthn passwordless authentication.
- **Regulatory requirements** may impose multi-factor authentication (MFA).

A monolithic authentication service that hard-codes all methods is brittle and violates
the Open/Closed Principle. Adding a new authentication standard (e.g., a future
post-quantum scheme) should not require modifying existing authentication logic.

The framework must also support **provider chaining** — trying multiple providers in
sequence until one succeeds — to enable fallback scenarios (e.g., try SSO first, fall
back to local credentials).

## Decision

We implement an **extensible authentication framework** using the **Chain of
Responsibility** pattern, located in `com.erp.kernel.security.auth`.

### Provider types

The `AuthenticationProviderType` enum defines the supported authentication mechanisms:

| Type         | Implementation Class           | Mechanism                        |
|--------------|--------------------------------|----------------------------------|
| `LOCAL`      | `LocalAuthenticationProvider`  | SHA-256 hashed password with salt|
| `LDAP`       | `LdapAuthenticationProvider`   | LDAP/Active Directory bind       |
| `SAML`       | *(via SsoService)*             | SAML 2.0 assertion validation    |
| `OIDC`       | *(via SsoService)*             | OpenID Connect token exchange    |
| `WEBAUTHN`   | `WebAuthnService`              | FIDO2 challenge-response         |

### Core interfaces

```java
public interface AuthenticationProvider {
    AuthenticationResult authenticate(String username, String credential);
    boolean supports(AuthenticationProviderType type);
}
```

Each provider implements `authenticate()` to perform its specific verification and
`supports()` to declare which provider type it handles.

### Provider chain execution

`AuthenticationService` orchestrates the chain:

1. Receive authentication request with `username` and `credential`.
2. Iterate through registered `AuthenticationProvider` instances in priority order.
3. For each provider, call `authenticate(username, credential)`.
4. If a provider returns a successful `AuthenticationResult`, stop and return it.
5. If the provider fails, continue to the next provider in the chain.
6. If all providers fail, return an authentication failure result.
7. If the successful result indicates MFA is required, delegate to `MfaService`.

### Result model

`AuthenticationResult` is an immutable Java record:

| Field              | Type                        | Description                       |
|--------------------|-----------------------------|-----------------------------------|
| `success`          | `boolean`                   | Whether authentication succeeded  |
| `username`         | `String`                    | Authenticated user identity       |
| `providerType`     | `AuthenticationProviderType`| Which provider authenticated      |
| `mfaRequired`      | `boolean`                   | Whether MFA step is needed        |

### Supporting services

| Service             | Package                  | Responsibility                     |
|---------------------|--------------------------|------------------------------------|
| `LdapProperties`    | `security.ldap`          | LDAP connection configuration      |
| `SsoService`        | `security.sso`           | SAML/OIDC token processing         |
| `SsoProviderType`   | `security.sso`           | Enum: `SAML`, `OIDC`              |
| `WebAuthnService`   | `security.service`       | FIDO2 registration and assertion   |
| `MfaService`        | `security.service`       | TOTP-based second factor           |

## Consequences

### Positive

- **Extensibility**: New authentication methods are added by implementing
  `AuthenticationProvider` and registering as a Spring bean — zero changes to
  existing code.
- **Fallback resilience**: Provider chaining ensures users can authenticate even if
  their primary provider (e.g., LDAP server) is temporarily unavailable.
- **Separation of concerns**: Each provider encapsulates its own authentication
  logic, dependencies, and configuration.
- **MFA integration**: The `mfaRequired` flag in `AuthenticationResult` cleanly
  separates first-factor and second-factor flows.
- **Testability**: Each provider is independently unit-testable with mocked
  dependencies.

### Negative

- **Chain ordering**: Provider priority is implicit in the Spring bean registration
  order. Misconfiguration could cause unintended fallback behaviour. Mitigated by
  explicit ordering documentation and configuration properties.
- **Latency accumulation**: If early providers in the chain are slow to fail, total
  authentication latency increases. Mitigated by implementing timeouts per provider.
- **Credential format variance**: Different providers expect different credential
  formats (password string vs. WebAuthn assertion). Mitigated by the generic
  `String credential` parameter with provider-specific parsing.

---

*Relates to: Phase 4 milestones 4.1–4.7. Builds on ADR-0001 (Java records for
AuthenticationResult).*
