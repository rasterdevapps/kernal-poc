# Architecture Decision Records (ADRs)

> **Project:** ERP Kernel PoC
> **Phase:** 9.3 — Framework Documentation

This directory contains Architecture Decision Records (ADRs) documenting the
significant technical decisions made during the development of the ERP Kernel
framework. Each ADR follows the standard format: **Title, Status, Context, Decision,
Consequences**.

---

## Index

| ADR | Title | Status | Phase |
|-----|-------|--------|-------|
| [ADR-0001](0001-java-25-spring-boot.md) | Java 25 with Spring Boot 3.4 Technology Stack | Accepted | Phase 1 |
| [ADR-0002](0002-ansi-sparc-three-schema.md) | ANSI/SPARC Three-Schema Database Architecture | Accepted | Phase 2 |
| [ADR-0003](0003-plugin-extension-architecture.md) | Plugin and Extension Point Architecture | Accepted | Phase 9 |
| [ADR-0004](0004-authentication-provider-chain.md) | Extensible Authentication Provider Chain | Accepted | Phase 4 |
| [ADR-0005](0005-multi-vertical-readiness.md) | Multi-Vertical Deployment Readiness | Accepted | Phase 9 |
| [ADR-0006](0006-performance-benchmarking.md) | Performance Benchmarking Subsystem | Accepted | Phase 9 |

---

## ADR Descriptions

### [ADR-0001 — Java 25 with Spring Boot 3.4](0001-java-25-spring-boot.md)

Documents the choice of Java 25 and Spring Boot 3.4.4 as the technology stack,
including the decision to exclude Lombok due to its incompatibility with Java 25's
removal of `sun.misc.Unsafe`. Covers build tooling, testing libraries, and coverage
enforcement.

### [ADR-0002 — ANSI/SPARC Three-Schema Architecture](0002-ansi-sparc-three-schema.md)

Documents the adoption of the ANSI/SPARC three-schema model (External, Conceptual,
Internal) for the database abstraction layer, inspired by SAP's Data Dictionary
(DDIC/SE11). Maps schema levels to implementation components.

### [ADR-0003 — Plugin and Extension Point Architecture](0003-plugin-extension-architecture.md)

Documents the plugin system with managed lifecycle states (CREATED → INITIALIZED →
STARTED → STOPPED → DESTROYED) and the typed extension registry pattern using
`ConcurrentHashMap` for thread safety.

### [ADR-0004 — Extensible Authentication Provider Chain](0004-authentication-provider-chain.md)

Documents the Chain of Responsibility pattern for authentication, supporting LOCAL,
LDAP, SAML, OIDC, and WEBAUTHN provider types. Each provider implements the
`AuthenticationProvider` interface.

### [ADR-0005 — Multi-Vertical Deployment Readiness](0005-multi-vertical-readiness.md)

Documents the framework design for multi-vertical deployment with `VerticalType` enum
(HEALTHCARE, MANUFACTURING, RETAIL, FINANCE, EDUCATION) and `VerticalDescriptor`
validation of required framework capabilities.

### [ADR-0006 — Performance Benchmarking Subsystem](0006-performance-benchmarking.md)

Documents the embedded benchmarking subsystem with `BenchmarkScenario` interface,
`BenchmarkService` execution engine, configurable `BenchmarkProperties`, and REST API
for CI/CD pipeline integration.

---

## Creating New ADRs

When adding a new ADR:

1. Use the next sequential number: `NNNN-short-title.md`.
2. Follow the standard format: Title, Status, Context, Decision, Consequences.
3. Reference the relevant Phase and Milestone numbers.
4. Update this index with a link and brief description.
5. Set the status to one of: **Proposed**, **Accepted**, **Deprecated**, **Superseded**.
