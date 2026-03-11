# ADR-0001: Java 25 with Spring Boot 3.4 Technology Stack

| Field       | Value                                      |
|-------------|--------------------------------------------|
| **Status**  | Accepted                                   |
| **Date**    | 2025-01-15                                 |
| **Phase**   | Phase 1 — Foundation & Infrastructure Setup |
| **Milestone** | 1.1 — Project Scaffolding                |

---

## Context

The ERP Kernel project requires a robust, long-term technology foundation capable of
supporting enterprise workloads across multiple industry verticals (healthcare,
manufacturing, retail, finance, education). The chosen stack must provide:

- Strong type safety and modern language features for complex business logic.
- A mature ecosystem with proven libraries for security, data access, and web services.
- Long-term support and active community maintenance.
- Compatibility with PostgreSQL 18 and H2DB for dual-profile database access.
- High performance for concurrent, multi-tenant enterprise operations.

Java 25 introduces several language enhancements (pattern matching, sealed classes,
virtual threads, record patterns) that directly benefit enterprise application
development. However, Java 25 removes `sun.misc.Unsafe`, which breaks Lombok's
bytecode generation at compile time.

## Decision

We adopt **Java 25** as the runtime and compilation target and **Spring Boot 3.4.4** as
the application framework for the ERP Kernel.

### Key choices

| Aspect                  | Decision                                            |
|-------------------------|-----------------------------------------------------|
| **Language version**    | Java 25 (`JavaLanguageVersion.of(25)`)              |
| **Framework**           | Spring Boot 3.4.4 with Spring Dependency Management |
| **Build tool**          | Gradle 8.13 with Gradle Wrapper                     |
| **ORM**                 | Spring Data JPA (Hibernate)                         |
| **Database migration**  | Flyway Core with PostgreSQL driver                  |
| **Caching**             | Caffeine (in-process, high-performance)             |
| **JWT**                 | JJWT 0.12.6                                        |
| **API documentation**   | SpringDoc OpenAPI 2.8.4                             |
| **Testing**             | JUnit 5, Mockito 5.18, AssertJ, ByteBuddy 1.17     |
| **Coverage**            | JaCoCo 0.8.14 — 100% line and branch enforcement   |
| **Lombok**              | **Not used** — incompatible with Java 25            |

### Lombok exclusion rationale

Lombok relies on internal JDK APIs (`sun.misc.Unsafe`) for compile-time bytecode
manipulation. Java 25 removes `sun.misc.Unsafe` entirely, making Lombok incompatible.
Instead we use:

- **Java records** for immutable data carriers (DTOs, value objects, configuration).
- **Explicit constructors** for dependency injection (constructor injection only).
- **IDE-generated** or manually written `equals`, `hashCode`, `toString` where needed.

This approach eliminates the compile-time annotation processing dependency, improves
debuggability, and ensures forward compatibility with future JDK releases.

## Consequences

### Positive

- **Future-proof**: Java 25 features (virtual threads, pattern matching, sealed classes)
  reduce boilerplate and improve expressiveness without third-party tools.
- **No hidden magic**: Removing Lombok makes the codebase fully transparent — what you
  read in source is what gets compiled.
- **Ecosystem maturity**: Spring Boot 3.4 provides production-grade starters for web,
  data, security, caching, WebSocket, and actuator health monitoring.
- **Testing confidence**: JaCoCo enforcement at 100% coverage ensures every code path
  is exercised, enabled by ByteBuddy 1.17 compatibility with Java 25.
- **Standards compliance**: Aligns with ISO/IEC/IEEE 12207 implementation requirements
  and CMMI Level 5 quality assurance practices.

### Negative

- **Verbosity**: Without Lombok, entity classes and DTOs require explicit accessor
  methods and constructors. Mitigated by using Java records where immutability is
  appropriate.
- **Early adoption risk**: Java 25 is newer than the widely-deployed Java 21 LTS.
  Mitigated by the project's CI/CD pipeline catching regressions early.
- **Dependency compatibility**: Some third-party libraries may lag in Java 25 support.
  Mitigated by pinning ByteBuddy and Mockito versions explicitly in the build.

### Risks

- If a critical library fails on Java 25, we may need to contribute patches upstream
  or find alternatives. This risk is tracked and monitored per milestone.

---

*Relates to: Phase 1 milestones 1.1–1.5. Supersedes no prior decision.*
