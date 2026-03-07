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