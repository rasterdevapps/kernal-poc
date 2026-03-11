# Getting Started — ERP Kernel PoC

> **For freshers and new developers joining the project.**
> This guide walks you through everything you need to understand, set up, and start contributing to the ERP Kernel PoC.

---

## Table of Contents

1. [What is this application?](#1-what-is-this-application)
2. [Technology overview](#2-technology-overview)
3. [Prerequisites](#3-prerequisites)
4. [Setting up your development environment](#4-setting-up-your-development-environment)
5. [Running the backend](#5-running-the-backend)
6. [Running the frontend](#6-running-the-frontend)
7. [Exploring the application](#7-exploring-the-application)
8. [Understanding the project structure](#8-understanding-the-project-structure)
9. [Key concepts explained simply](#9-key-concepts-explained-simply)
10. [Making your first code change](#10-making-your-first-code-change)
11. [Writing and running tests](#11-writing-and-running-tests)
12. [Common developer tasks](#12-common-developer-tasks)
13. [Glossary](#13-glossary)

---

## 1. What is this application?

The **ERP Kernel PoC** (Proof of Concept) is the foundation of an **Enterprise Resource Planning (ERP) system** — similar in concept to SAP, but built from scratch using modern Java and web technologies.

Think of it as a **platform** rather than a finished product. It provides the core building blocks that other business applications (called *verticals*) can be built on top of. These verticals include industries like healthcare, manufacturing, retail, finance, and education.

### What does it currently do?

| Feature | Description |
|---------|-------------|
| **User management** | Create users, assign roles and permissions |
| **Authentication** | Login with username/password, two-factor authentication, or hardware keys |
| **Data Dictionary** | Define database tables and fields without writing SQL |
| **Navigation** | SAP-inspired T-code shortcuts for navigating to screens |
| **Number Ranges** | Automatic, sequential number generation for business documents |
| **Themes** | Switch between light, dark, and custom UI themes |
| **Personalisation** | Save favourite screens and view recent navigation history |
| **System health** | Monitor application health, metrics, and backups |

### Why is it a "PoC"?

A Proof of Concept proves that the *architecture and approach* work before building real business features on top. This PoC has all 45 milestones completed across 9 phases, from basic infrastructure to enterprise-grade resilience and performance benchmarking.

---

## 2. Technology overview

You do not need to be an expert in all of these, but you should recognise what each one does.

### Backend (Server-side)

| Technology | Version | What it does |
|------------|---------|--------------|
| **Java** | 25 | The main programming language |
| **Spring Boot** | 3.4.4 | Framework that makes building REST APIs easy |
| **Gradle** | 8.13 | Build tool — compiles, tests, and packages the application |
| **H2** | — | In-memory database used during local development |
| **PostgreSQL** | 18 | The real database used in production |
| **Flyway** | 10.x | Automatically applies database changes (migrations) |
| **JUnit 5** | — | Testing framework |
| **JaCoCo** | 0.8.14 | Measures how much of the code is covered by tests |

### Frontend (Browser-side)

| Technology | Version | What it does |
|------------|---------|--------------|
| **Angular** | 19.2 | The web application framework |
| **TypeScript** | 5.7 | A typed superset of JavaScript |
| **Node.js & npm** | 18+ | Required to build and run the Angular app |

### Infrastructure

| Technology | What it does |
|------------|--------------|
| **GitHub Actions** | Automatically builds and tests on every code push |
| **OpenAPI / Swagger** | Auto-generated interactive API documentation |
| **Spring Boot Actuator** | Health check and metrics endpoints |

---

## 3. Prerequisites

Before you can run anything, you need to install the following tools on your computer.

### Required for everyone

#### 1. Java 25 (JDK)

The backend is written in Java 25. You need the full JDK (not just the JRE).

**Download:** [https://adoptium.net/](https://adoptium.net/)
Select: **Java 25**, **JDK**, your operating system.

Verify after installation:
```bash
java -version
# Expected: openjdk version "25" ...
```

#### 2. Git

Version control tool for downloading and managing the code.

**Download:** [https://git-scm.com/](https://git-scm.com/)

Verify:
```bash
git --version
# Expected: git version 2.x.x
```

#### 3. Node.js and npm (for the frontend)

Required to run and build the Angular frontend.

**Download:** [https://nodejs.org/](https://nodejs.org/) — choose the **LTS** version (18 or above).

Verify:
```bash
node --version    # Expected: v18.x.x or higher
npm --version     # Expected: 9.x.x or higher
```

### Optional (for production only)

**PostgreSQL 18** — only needed if you want to run the application against a real database. For local development, an H2 in-memory database is used automatically — no installation required.

---

## 4. Setting up your development environment

### Step 1 — Clone the repository

```bash
git clone https://github.com/rasterdevapps/kernal-poc.git
cd kernal-poc
```

### Step 2 — Verify the backend build

The project uses the Gradle wrapper (`gradlew`), which downloads Gradle automatically on first use. You do not need to install Gradle separately.

```bash
# On macOS / Linux:
./gradlew build

# On Windows:
gradlew.bat build
```

> **First run note:** Gradle will download all dependencies (~200 MB). This only happens once.

A successful build ends with:
```
BUILD SUCCESSFUL in Xs
```

### Step 3 — Install frontend dependencies

```bash
cd frontend
npm install
cd ..
```

> **First run note:** npm will download all Angular dependencies (~500 MB into `node_modules`). This also only happens once.

---

## 5. Running the backend

### Quick start

```bash
./gradlew bootRun
```

The server starts on port **8080**. You will see output like:

```
  .   ____          _
 /\\ / ___'_ __ _ _(_)_ __  __ _
( ( )\___ | '_ | '_| | '_ \/ _` |
 \\/  ___)| |_)| | | | | || (_| |  {}
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.4.4)

...
Started KernelApplication in 3.456 seconds
```

### What runs locally

| URL | What it is |
|-----|-----------|
| `http://localhost:8080` | Backend API (JSON responses) |
| `http://localhost:8080/api/v1/health` | Health check — confirm the server is running |
| `http://localhost:8080/swagger-ui.html` | Interactive API documentation |
| `http://localhost:8080/h2-console` | Database browser (local dev only) |
| `http://localhost:8080/actuator/health` | Spring Boot internal health check |

### Verifying the backend is running

Open your browser or use `curl`:

```bash
curl http://localhost:8080/api/v1/health
```

You should see a JSON response:
```json
{
  "status": "UP",
  "application": "erp-kernel",
  "profile": "local"
}
```

### How the local database works

By default the application uses the **`local` Spring profile**, which configures an H2 in-memory database. This means:

- **No PostgreSQL installation needed.**
- The database is created fresh each time the application starts.
- Flyway automatically runs all migration scripts (V1 through V7) to create the tables.
- You can browse the database at `http://localhost:8080/h2-console`.

**H2 console login settings:**
| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:erp_kernel` |
| Username | `sa` |
| Password | *(leave blank)* |

### Stopping the application

Press **Ctrl + C** in the terminal where `bootRun` is running.

---

## 6. Running the frontend

Open a **new terminal window** (keep the backend running in the other one):

```bash
cd frontend
npm start
```

The Angular development server starts on port **4200**:

```
Initial chunk files | Names          | Raw size
chunk-...           | main           | 1.20 MB
...
Application bundle generation complete.
✔ Server listening on: http://localhost:4200/
```

Open your browser and go to: **`http://localhost:4200`**

### How the frontend talks to the backend

The frontend is configured to **proxy API requests** from port 4200 to port 8080.
This is set up in `frontend/proxy.conf.json`:

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

So when the Angular app calls `/api/v1/health`, the request is automatically forwarded to `http://localhost:8080/api/v1/health`. Both servers must be running at the same time.

---

## 7. Exploring the application

### API documentation (Swagger UI)

The easiest way to explore the backend is through the **Swagger UI** at:
`http://localhost:8080/swagger-ui.html`

Here you can:
- See all available API endpoints grouped by category.
- Read request and response formats for each endpoint.
- Click **"Try it out"** to send test requests directly from the browser.

### Key API endpoints to try first

All endpoints below can be tested without authentication in the local profile.

#### Health check
```http
GET http://localhost:8080/api/v1/health
```

#### List T-codes (navigation shortcuts)
```http
GET http://localhost:8080/api/v1/navigation/tcodes
```

#### List themes
```http
GET http://localhost:8080/api/v1/navigation/themes
```

#### Get system variables (SY-* values)
```http
GET http://localhost:8080/api/v1/system/variables
```

#### Framework information
```http
GET http://localhost:8080/api/v1/framework
```

### The Angular UI

After starting both the backend and frontend, visit `http://localhost:4200` to see the web application. The main screens are:

| Screen | URL | Description |
|--------|-----|-------------|
| Dashboard | `/dashboard` | Home screen with favourites and recent items |
| T-Code Manager | `/admin/tcodes` | Manage navigation shortcuts |
| Theme Manager | `/admin/themes` | Manage UI themes |
| Screen Manager | `/admin/screens` | Manage screen definitions |
| Naming Templates | `/admin/naming-templates` | Manage naming conventions |
| Preferences | `/preferences` | User personalisation settings |

---

## 8. Understanding the project structure

### Top-level layout

```
kernal-poc/
├── src/                        # Java backend source code
├── frontend/                   # Angular frontend source code
├── docs/                       # Documentation and ADRs
├── build.gradle                # Gradle build configuration
├── settings.gradle             # Project name and settings
├── gradle/                     # Gradle wrapper files
├── gradlew / gradlew.bat       # Gradle wrapper scripts (use these to build)
├── README.md                   # Project overview and quick start
├── MILESTONES.md               # Project roadmap and phase status
└── .github/
    └── workflows/ci.yml        # GitHub Actions CI/CD pipeline
```

### Backend source code (`src/`)

```
src/
├── main/
│   ├── java/com/erp/kernel/
│   │   ├── KernelApplication.java         # Entry point — starts Spring Boot
│   │   ├── health/                        # /api/v1/health endpoint
│   │   ├── api/                           # REST API config, JWT filter, CORS, WebSocket
│   │   ├── security/                      # Authentication, users, roles, MFA, SSO
│   │   ├── ddic/                          # Data Dictionary (table/field/domain definitions)
│   │   ├── datatypes/                     # Elementary, complex, and reference data types
│   │   ├── numberrange/                   # Number range management
│   │   ├── navigation/                    # T-codes, themes, favourites, preferences
│   │   ├── businesslogic/                 # Rules engine and event processing
│   │   ├── plugin/                        # Plugin lifecycle management
│   │   ├── vertical/                      # Multi-industry vertical support
│   │   ├── resilience/                    # Backups, disaster recovery, circuit breakers
│   │   ├── benchmark/                     # Performance benchmarking
│   │   ├── encryption/                    # AES-256 data encryption
│   │   ├── sysvar/                        # System variables (SY-DATUM, SY-UNAME, etc.)
│   │   ├── framework/                     # Framework info endpoint
│   │   └── config/                        # Spring configuration classes
│   └── resources/
│       ├── application.yml                # Base configuration (shared)
│       ├── application-local.yml          # Local development config (H2 database)
│       ├── application-prod.yml           # Production config (PostgreSQL)
│       └── db/migration/                  # Flyway SQL migration scripts
│           ├── V1__init_schema.sql
│           ├── V2__ddic_schema.sql
│           ├── V3__number_range_schema.sql
│           ├── V4__auth_schema.sql
│           ├── V5__api_schema.sql
│           ├── V6__navigation_schema.sql
│           └── V7__resilience_schema.sql
└── test/
    ├── java/com/erp/kernel/               # Test classes (mirror the main structure)
    └── resources/
        └── application-test.yml           # Test profile config (H2 database)
```

### Frontend source code (`frontend/`)

```
frontend/
├── src/
│   ├── app/
│   │   ├── layout/             # Shared UI chrome
│   │   │   ├── header/         # Top navigation bar
│   │   │   ├── sidebar/        # Left navigation panel
│   │   │   ├── footer/         # Bottom bar
│   │   │   └── bottom-nav/     # Mobile bottom navigation
│   │   ├── pages/              # Individual screen components
│   │   │   ├── dashboard/
│   │   │   ├── tcode-list/
│   │   │   ├── theme-list/
│   │   │   ├── screen-list/
│   │   │   ├── naming-template-list/
│   │   │   └── preferences/
│   │   ├── services/           # Angular services (talk to the backend API)
│   │   │   ├── tcode.service.ts
│   │   │   ├── theme.service.ts
│   │   │   ├── favourite.service.ts
│   │   │   ├── recent-navigation.service.ts
│   │   │   ├── user-preference.service.ts
│   │   │   ├── screen.service.ts
│   │   │   ├── naming-template.service.ts
│   │   │   └── breakpoint.service.ts
│   │   ├── models/             # TypeScript data models (match backend DTOs)
│   │   └── app.routes.ts       # Application routing definitions
│   ├── environments/           # Environment-specific config (dev vs prod)
│   └── index.html              # Single HTML page entry point
├── angular.json                # Angular project configuration
├── package.json                # Node.js dependency list
├── tsconfig.json               # TypeScript compiler settings
└── proxy.conf.json             # Proxy API calls from :4200 to :8080
```

### Backend layered architecture

Every feature follows the same three-layer pattern:

```
HTTP Request
     │
     ▼
┌──────────────────┐
│   Controller     │  Handles HTTP — maps URLs to Java methods
│  (@RestController)│  Returns HTTP responses (JSON)
└────────┬─────────┘
         │ calls
         ▼
┌──────────────────┐
│    Service       │  Contains business logic — what the app actually does
│  (@Service)      │  Validates data, applies rules, coordinates operations
└────────┬─────────┘
         │ calls
         ▼
┌──────────────────┐
│   Repository     │  Talks to the database — reads and writes data
│  (@Repository)   │  Uses Spring Data JPA (no SQL needed for basic operations)
└────────┬─────────┘
         │
         ▼
   Database (H2 / PostgreSQL)
```

**Example — how a GET /api/v1/navigation/themes request flows:**

1. `ThemeController.getThemes()` receives the HTTP GET request.
2. It calls `ThemeService.getAllThemes()`.
3. `ThemeService` calls `ThemeRepository.findAll()`.
4. The repository queries the database and returns a list of theme rows.
5. The service maps the rows to DTOs (Data Transfer Objects — simple data containers).
6. The controller returns the DTOs as a JSON array.

---

## 9. Key concepts explained simply

### What is a T-code?

A **T-code** (Transaction Code) is a short keyword that navigates directly to a screen — inspired by SAP. Instead of clicking through menus, you type the T-code and jump straight there. For example, `SU01` might navigate to the User Management screen.

### What is the Data Dictionary (DDIC)?

The **Data Dictionary** is a metadata store for the database schema. Instead of writing SQL `CREATE TABLE` statements directly, developers define tables, fields, domains, and data elements through the application. This makes the schema self-describing and allows for features like custom extensions (Z-fields) without modifying the core.

### What is a Spring Profile?

A **Spring Profile** is a named configuration set. This project uses three profiles:

| Profile | When used | Database |
|---------|-----------|----------|
| `local` (default) | Your laptop | H2 in-memory |
| `test` | Automated tests | H2 in-memory |
| `prod` | Production server | PostgreSQL 18 |

The `local` profile is active by default. You do not need to do anything special to use it.

### What is Flyway?

**Flyway** is a database migration tool. When the application starts, Flyway automatically runs any SQL migration scripts that have not yet been applied to the database. Scripts are numbered (`V1__`, `V2__`, ...) and run in order. This ensures every developer and every environment always has an identical database structure.

### What is JWT?

**JWT** (JSON Web Token) is how the API proves that a request comes from an authenticated user. When a user logs in, the server returns a JWT token (a long encoded string). The client sends this token in the `Authorization` header with every subsequent request:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

In the local development environment, most endpoints do not require authentication to make it easier to explore.

### What is RBAC?

**RBAC** (Role-Based Access Control) is the authorisation system. Instead of granting permissions directly to each user, users are assigned **roles** (e.g., `ADMIN`, `USER`, `VIEWER`), and roles have **permissions** (e.g., `USER:READ`, `TABLE:WRITE`). This makes it easy to manage access for large numbers of users.

### What is a Plugin?

A **Plugin** is a self-contained module that can be added to the ERP Kernel without modifying the core framework. Plugins go through a managed lifecycle: `CREATED → INITIALIZED → STARTED → STOPPED → DESTROYED`. Industry verticals (healthcare, manufacturing, etc.) are implemented as plugins.

---

## 10. Making your first code change

This section walks through adding a simple feature: **a new health detail field to the health check response**.

### Step 1 — Find the relevant code

The health check is at `/api/v1/health`. Search for the controller:

```bash
# On macOS/Linux:
grep -r "health" src/main/java --include="*.java" -l

# You should find:
# src/main/java/com/erp/kernel/health/HealthController.java
```

### Step 2 — Understand the existing code

Open `src/main/java/com/erp/kernel/health/HealthController.java`. You will see a pattern like:

```java
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> getHealth() {
        // returns a HealthResponse record
    }
}
```

### Step 3 — Find the tests

Every class has a corresponding test class. Tests are in `src/test/java/` in the same package path:

```
src/test/java/com/erp/kernel/health/HealthControllerTest.java
```

### Step 4 — Create a feature branch

Always work on a feature branch, never directly on `main`:

```bash
git checkout -b feature/my-first-change
```

### Step 5 — Make your change

Follow the pattern you observed. Key rules:
- Controllers handle HTTP only — no business logic.
- Use `record` types for DTOs (immutable data containers).
- Write Javadoc on every public method.
- Use `final` for fields and local variables that do not change.

### Step 6 — Write a test for your change

New code requires a test. Here is an example test structure to follow:

```java
@ExtendWith(MockitoExtension.class)
class MyNewServiceTest {

    @Mock
    private SomeDependency dependency;

    @InjectMocks
    private MyNewService service;

    @Test
    void shouldReturnExpectedResult_whenConditionIsMet() {
        // Arrange
        when(dependency.getData()).thenReturn("expected");

        // Act
        var result = service.doSomething();

        // Assert
        assertThat(result).isEqualTo("expected");
    }
}
```

### Step 7 — Run the tests

```bash
./gradlew test
```

If the tests pass, verify coverage:

```bash
./gradlew jacocoTestReport jacocoTestCoverageVerification
```

The HTML coverage report is generated at:
`build/reports/jacoco/test/html/index.html`

### Step 8 — Commit and push

```bash
git add .
git commit -m "Add #<issue-number>: <description in imperative mood>"
git push origin feature/my-first-change
```

Then open a Pull Request on GitHub targeting the `main` branch.

---

## 11. Writing and running tests

### Running all tests

```bash
./gradlew test
```

### Running a specific test class

```bash
./gradlew test --tests "com.erp.kernel.health.HealthControllerTest"
```

### Running a specific test method

```bash
./gradlew test --tests "com.erp.kernel.health.HealthControllerTest.shouldReturnHealthy_whenApplicationIsUp"
```

### Generating the coverage report

```bash
./gradlew jacocoTestReport
```

Open `build/reports/jacoco/test/html/index.html` in a browser to see which lines are covered (green) and which are not (red).

### Coverage requirements

The project requires **100% line coverage and 100% branch coverage** for all code. The CI/CD pipeline will fail if coverage drops below this threshold. This means:
- Every line of new code you write must be executed by at least one test.
- Every `if/else` branch, `switch` case, and ternary expression must be tested for all outcomes.

### Test file naming conventions

| Class | Corresponding test class |
|-------|--------------------------|
| `UserService.java` | `UserServiceTest.java` |
| `HealthController.java` | `HealthControllerTest.java` |
| `ThemeRepository.java` | `ThemeRepositoryTest.java` |

Tests live in `src/test/java/` following the same package path as the class under test.

### Test patterns used in this project

#### Unit test (Service layer)

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser_whenUserExists() {
        // Arrange
        var userId = 1L;
        var user = new User(userId, "alice");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        var result = userService.getUserById(userId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }
}
```

#### Controller test (`@WebMvcTest`)

```java
@WebMvcTest(HealthController.class)
@AutoConfigureMockMvc(addFilters = false)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldReturnHealthy_whenApplicationIsUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value("UP"));
    }
}
```

---

## 12. Common developer tasks

### Start both backend and frontend

Open two terminal windows:

**Terminal 1 — Backend:**
```bash
cd kernal-poc
./gradlew bootRun
```

**Terminal 2 — Frontend:**
```bash
cd kernal-poc/frontend
npm start
```

Then open `http://localhost:4200` in your browser.

### Full build and verification

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

### Check what endpoints are available

Visit `http://localhost:8080/swagger-ui.html` while the backend is running.

### View the database (local only)

Visit `http://localhost:8080/h2-console` while the backend is running.
- JDBC URL: `jdbc:h2:mem:erp_kernel`
- Username: `sa`
- Password: *(blank)*

### Add a new database table

1. Create a new Flyway migration script in `src/main/resources/db/migration/`:
   - Name it with the next version number (e.g., `V8__my_new_table.sql`).
   - Write the `CREATE TABLE` SQL inside it.
2. Restart the application — Flyway will automatically run the new script.

### Check application logs

When running `./gradlew bootRun`, all log output appears in the terminal. Look for:
- `INFO` — normal operational messages.
- `WARN` — potential issues to investigate.
- `ERROR` — failures that need attention.

### Update an Angular service to call a new backend endpoint

Angular services in `frontend/src/app/services/` use Angular's `HttpClient` to call the backend:

```typescript
// Example pattern from tcode.service.ts
@Injectable({ providedIn: 'root' })
export class MyNewService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/my-endpoint';

  getAll(): Observable<MyModel[]> {
    return this.http.get<MyModel[]>(this.apiUrl);
  }
}
```

### View CI/CD pipeline results

Go to the repository on GitHub → **Actions** tab to see the status of all automated builds and tests.

---

## 13. Glossary

| Term | Meaning |
|------|---------|
| **ADR** | Architecture Decision Record — a document explaining why a technical decision was made |
| **API** | Application Programming Interface — a set of URLs the frontend calls to get/send data |
| **DTO** | Data Transfer Object — a simple data container used to send data between layers |
| **ERP** | Enterprise Resource Planning — business software that manages core processes |
| **Flyway** | Tool that automatically applies versioned SQL scripts to the database |
| **Gradle** | Build tool used to compile, test, and package the Java application |
| **H2** | An in-memory database used during local development (no installation needed) |
| **HTTP** | The protocol used to send data between the browser and the server |
| **JaCoCo** | Java code coverage tool — measures how much of the code is tested |
| **JPA** | Java Persistence API — lets you read/write database records as Java objects |
| **JWT** | JSON Web Token — a secure way to prove a user is authenticated |
| **MFA / 2FA** | Multi-Factor / Two-Factor Authentication — requires a second proof of identity beyond a password |
| **Migration** | A versioned SQL script that modifies the database schema |
| **Mock** | A fake version of a dependency used in unit tests |
| **OpenAPI / Swagger** | Standard for describing and documenting REST APIs; Swagger UI lets you test them interactively |
| **Plugin** | A self-contained module that extends the framework without changing the core |
| **PoC** | Proof of Concept — demonstrates that a design approach works |
| **Profile** | A named Spring Boot configuration set (e.g., `local`, `test`, `prod`) |
| **RBAC** | Role-Based Access Control — users get permissions through roles, not directly |
| **Record** | A Java language feature for creating immutable data classes concisely |
| **Repository** | The data access layer — reads and writes to the database |
| **REST** | Representational State Transfer — the API style used (GET/POST/PUT/DELETE over HTTP) |
| **Service** | The business logic layer — sits between the controller and the repository |
| **Spring Boot** | A Java framework that simplifies building REST APIs and web applications |
| **T-code** | Transaction Code — a short keyword that navigates directly to a screen (SAP-inspired) |
| **TypeScript** | A typed version of JavaScript used for the Angular frontend |
| **Vertical** | An industry-specific module built on top of the ERP Kernel (e.g., Healthcare, Manufacturing) |
| **WebAuthn / FIDO2** | A standard for passwordless authentication using hardware keys or device biometrics |
| **WebSocket** | A persistent connection between browser and server for real-time updates |

---

## Need help?

- Read the [Architecture Decision Records](adr/README.md) for deeper context on technical choices.
- Check the [MILESTONES.md](../MILESTONES.md) to understand what has been built.
- Browse the [Swagger UI](http://localhost:8080/swagger-ui.html) (backend running) to explore all available APIs.
- Ask a question by opening a GitHub Issue in the repository.
