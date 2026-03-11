# ERP Kernel PoC

> **Tech Stack:** Java 25 · Spring Boot 3.4 · PostgreSQL 18 · H2DB (local) · Flyway · Gradle · GitHub Actions

## Prerequisites

- **Java 25** (JDK) — download from [Adoptium](https://adoptium.net/)
- **PostgreSQL 18** — required for non-local environments
- **Gradle 8.13** — included via Gradle Wrapper (`./gradlew`)

## Quick Start

```bash
# Build the project
./gradlew build

# Run locally with H2 in-memory database
./gradlew bootRun

# Run tests
./gradlew test

# Generate coverage report (HTML at build/reports/jacoco/test/html/index.html)
./gradlew jacocoTestReport

# Verify coverage thresholds (100% line & branch)
./gradlew jacocoTestCoverageVerification
```

## Spring Profiles

| Profile | Database | Usage |
|---------|----------|-------|
| `local` (default) | H2 in-memory | Local development — H2 console at `/h2-console` |
| `prod` | PostgreSQL 18 | Production — configured via environment variables |
| `test` | H2 in-memory | Automated tests |

### Production Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/erp_kernel` |
| `DATABASE_USERNAME` | Database username | `erp_kernel` |
| `DATABASE_PASSWORD` | Database password | _(none)_ |

## Project Structure

```
src/
├── main/
│   ├── java/com/erp/kernel/          # Application source code
│   │   ├── KernelApplication.java    # Spring Boot entry point
│   │   └── health/                   # Health check controller
│   └── resources/
│       ├── application.yml           # Base configuration
│       ├── application-local.yml     # Local profile (H2)
│       ├── application-prod.yml      # Production profile (PostgreSQL)
│       └── db/migration/             # Flyway migration scripts
└── test/
    ├── java/com/erp/kernel/          # Test source code
    └── resources/
        └── application-test.yml      # Test profile (H2)
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/health` | Application health check |
| `GET` | `/actuator/health` | Spring Boot Actuator health |

## Database Versioning

Database schema changes are managed by [Flyway](https://flywaydb.org/).
Migration scripts are located in `src/main/resources/db/migration/` and follow the naming convention `V{version}__{description}.sql`.

## CI/CD

GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push and PR to `main`:

1. **Build** — compiles the project
2. **Test** — runs the full test suite
3. **Coverage** — generates JaCoCo report and verifies 100% line/branch coverage thresholds

## Developer Documentation

### Architecture Decision Records (ADRs)

All significant technical decisions are documented as ADRs in [`docs/adr/`](docs/adr/README.md):

| ADR | Title |
|-----|-------|
| [ADR-0001](docs/adr/0001-java-25-spring-boot.md) | Java 25 with Spring Boot 3.4 Technology Stack |
| [ADR-0002](docs/adr/0002-ansi-sparc-three-schema.md) | ANSI/SPARC Three-Schema Database Architecture |
| [ADR-0003](docs/adr/0003-plugin-extension-architecture.md) | Plugin and Extension Point Architecture |
| [ADR-0004](docs/adr/0004-authentication-provider-chain.md) | Extensible Authentication Provider Chain |
| [ADR-0005](docs/adr/0005-multi-vertical-readiness.md) | Multi-Vertical Deployment Readiness |
| [ADR-0006](docs/adr/0006-performance-benchmarking.md) | Performance Benchmarking Subsystem |

### Coding Standards

See [`.github/copilot-instructions.md`](.github/copilot-instructions.md) for the full coding standards, testing requirements, and process documentation including ISO/IEC/IEEE 12207 and CMMI Level 5 compliance.

## Getting Started — Onboarding Guide

> **New to this project?** See the full [Getting Started Guide for Freshers](docs/GETTING_STARTED.md) for a step-by-step walkthrough covering setup, running the application, understanding the code, writing tests, and a glossary of key terms.

New developers should follow these steps to get productive with the ERP Kernel:

### 1. Prerequisites

- Install **Java 25** (JDK) from [Adoptium](https://adoptium.net/).
- Install **PostgreSQL 18** if working with the `prod` profile (not needed for local development).
- Clone the repository and verify the build:

```bash
git clone <repository-url>
cd kernal-poc
./gradlew build
```

### 2. Run Locally

The `local` profile is active by default, using an H2 in-memory database:

```bash
./gradlew bootRun
```

- Application: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
- API Docs: `http://localhost:8080/swagger-ui.html`
- Health Check: `http://localhost:8080/api/v1/health`

### 3. Understand the Architecture

1. Read the ADRs in [`docs/adr/`](docs/adr/README.md) to understand key technical decisions.
2. Review the [MILESTONES.md](MILESTONES.md) for project scope and progress.
3. Explore the layered package structure:

| Package | Layer | Purpose |
|---------|-------|---------|
| `com.erp.kernel.ddic` | Data | ANSI/SPARC three-schema database abstraction |
| `com.erp.kernel.types` | Core | Elementary, complex, and reference data types |
| `com.erp.kernel.security` | Security | Authentication providers, RBAC, MFA |
| `com.erp.kernel.plugin` | Framework | Plugin lifecycle and extension registry |
| `com.erp.kernel.vertical` | Framework | Multi-vertical registration and validation |
| `com.erp.kernel.benchmark` | Operations | Performance benchmarking subsystem |
| `com.erp.kernel.health` | Operations | Health check endpoints |

### 4. Development Workflow

1. Create a feature branch: `feature/<short-description>`.
2. Follow the coding standards in `.github/copilot-instructions.md`.
3. Write tests achieving 100% line and branch coverage.
4. Run the full verification locally:

```bash
./gradlew test jacocoTestReport jacocoTestCoverageVerification
```

5. Submit a PR referencing the relevant GitHub issue and milestone.
6. Update `MILESTONES.md` status if completing a milestone.