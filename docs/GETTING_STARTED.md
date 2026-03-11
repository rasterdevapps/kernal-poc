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
9. [Key concepts — explained with real code from this project](#9-key-concepts--explained-with-real-code-from-this-project)
    - [9.1 Three-layer architecture](#91-three-layer-architecture-controller--service--repository)
    - [9.2 T-codes](#92-t-codes-transaction-codes)
    - [9.3 Data Dictionary and Z-fields](#93-data-dictionary-ddic-and-z-field-extensions)
    - [9.4 Spring Profiles](#94-spring-profiles--environment-switching)
    - [9.5 Flyway migrations](#95-flyway--database-migrations)
    - [9.6 JWT authentication](#96-jwt--api-authentication)
    - [9.7 RBAC](#97-rbac--role-based-access-control)
    - [9.8 Plugins and verticals](#98-plugins-and-verticals)
    - [9.9 Number Ranges](#99-number-ranges)
    - [9.10 Encryption](#910-encryption-aes-256-gcm)
    - [9.11 System Variables](#911-system-variables-sy-)
    - [9.12 Business Logic — Rules Engine](#912-business-logic--rules-engine)
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

## 9. Key concepts — explained with real code from this project

Each concept below includes a plain-language explanation followed by the **actual implementation** from this repository so you can see exactly how it works.

---

### 9.1 Three-layer architecture (Controller → Service → Repository)

Every feature in this project follows the same three-layer pattern. Understanding this pattern is the single most important thing for a fresher — once you see it, you can navigate the entire codebase.

```
HTTP Request  →  Controller  →  Service  →  Repository  →  Database
HTTP Response ←  Controller  ←  Service  ←  Repository  ←  Database
```

| Layer | Annotation | Responsibility | Example file |
|-------|-----------|----------------|--------------|
| **Controller** | `@RestController` | Handles HTTP requests and responses (JSON). No business logic. | `navigation/controller/ThemeController.java` |
| **Service** | `@Service` | Contains business logic, validation, caching, and transaction management. | `navigation/service/ThemeService.java` |
| **Repository** | extends `JpaRepository` | Talks to the database. Spring Data JPA generates the SQL for you. | `navigation/repository/ThemeRepository.java` |

#### Real example — Theme CRUD

**Controller** (`src/main/java/com/erp/kernel/navigation/controller/ThemeController.java`):

```java
@RestController
@RequestMapping("/api/v1/navigation/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = Objects.requireNonNull(themeService);
    }

    @PostMapping
    public ResponseEntity<ThemeDto> create(
            @Valid @RequestBody final CreateThemeRequest request) {
        final var created = themeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDto> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(themeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ThemeDto>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThemeDto> update(
            @PathVariable final Long id,
            @Valid @RequestBody final CreateThemeRequest request) {
        return ResponseEntity.ok(themeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Service** (`src/main/java/com/erp/kernel/navigation/service/ThemeService.java`):

```java
@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = Objects.requireNonNull(themeRepository);
    }

    @Transactional
    @CacheEvict(value = "themes", allEntries = true)
    public ThemeDto create(final CreateThemeRequest request) {
        Objects.requireNonNull(request);
        if (themeRepository.existsByThemeName(request.themeName())) {
            throw new DuplicateEntityException(
                "Theme with name '%s' already exists".formatted(request.themeName()));
        }
        final var entity = ThemeMapper.toEntity(request);
        final var saved = themeRepository.save(entity);
        return ThemeMapper.toDto(saved);
    }

    @Cacheable(value = "themes", key = "#id")
    public ThemeDto findById(final Long id) {
        Objects.requireNonNull(id);
        return themeRepository.findById(id)
            .map(ThemeMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(
                "Theme with id %d not found".formatted(id)));
    }

    @Cacheable(value = "themes", key = "'all'")
    public List<ThemeDto> findAll() {
        return themeRepository.findAll().stream()
            .map(ThemeMapper::toDto)
            .toList();
    }
}
```

**Entity and Repository** (`src/main/java/com/erp/kernel/navigation/entity/Theme.java`):

```java
@Entity
@Table(name = "nav_theme")
public class Theme extends BaseEntity {

    @Column(name = "theme_name", nullable = false, unique = true, length = 100)
    private String themeName;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor;      // Hex colour: #FF5733

    @Column(name = "secondary_color", nullable = false, length = 7)
    private String secondaryColor;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    // Getters and setters...
}

// Repository — Spring Data JPA generates the SQL automatically
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByThemeName(String themeName);
    boolean existsByThemeName(String themeName);
}
```

**Try it yourself** — with the backend running, create a theme using `curl`:

```bash
curl -X POST http://localhost:8080/api/v1/navigation/themes \
  -H "Content-Type: application/json" \
  -d '{
    "themeName": "Dark Mode",
    "description": "A dark colour scheme",
    "primaryColor": "#1e1e1e",
    "secondaryColor": "#007bff",
    "active": true,
    "isDefault": false
  }'
```

Then list all themes:

```bash
curl http://localhost:8080/api/v1/navigation/themes
```

> **Pattern to remember:** Every feature in this project follows the same Controller → Service → Repository structure. When you need to add a new feature, copy an existing one (e.g., Theme) and modify it.

---

### 9.2 T-codes (Transaction Codes)

A **T-code** is a short keyword that navigates directly to a screen — inspired by SAP. Instead of clicking through menus, a user types a code like `SU01` and jumps straight to the User Management screen.

#### Where it lives in the project

```
src/main/java/com/erp/kernel/navigation/
├── entity/TCode.java              # Database entity
├── dto/TCodeDto.java               # Data transfer object (record)
├── dto/CreateTCodeRequest.java     # Request body for creating a T-code
├── mapper/TCodeMapper.java         # Converts between entity and DTO
├── repository/TCodeRepository.java # Database access
├── service/TCodeService.java       # Business logic + caching
└── controller/TCodeController.java # REST API endpoints
```

#### How T-codes are stored (entity)

```java
@Entity
@Table(name = "nav_tcode")
public class TCode extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;            // e.g., "SU01", "SE11", "SPRO"

    @Column(name = "description", nullable = false, length = 500)
    private String description;     // "User Management"

    @Column(name = "module", nullable = false, length = 50)
    private String module;          // "Security", "DDIC", "Admin"

    @Column(name = "route", nullable = false, length = 500)
    private String route;           // "/admin/users" — the screen URL

    @Column(name = "icon", length = 100)
    private String icon;            // Material icon name

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
```

#### How T-codes are returned to the client (DTO)

```java
public record TCodeDto(
    Long id,
    String code,
    String description,
    String module,
    String route,
    String icon,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
```

#### Key service methods (with caching)

```java
@Service
@Transactional(readOnly = true)
public class TCodeService {

    @Transactional
    @CacheEvict(value = "tcodes", allEntries = true)
    public TCodeDto create(final CreateTCodeRequest request) {
        if (tCodeRepository.existsByCode(request.code())) {
            throw new DuplicateEntityException(
                "T-Code with code '%s' already exists".formatted(request.code()));
        }
        final var entity = TCodeMapper.toEntity(request);
        final var saved = tCodeRepository.save(entity);
        return TCodeMapper.toDto(saved);
    }

    @Cacheable(value = "tcodes", key = "'code:' + #code")
    public TCodeDto findByCode(final String code) {
        return tCodeRepository.findByCode(code)
            .map(TCodeMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(
                "T-Code with code '%s' not found".formatted(code)));
    }
}
```

#### API endpoints

```
POST   /api/v1/navigation/tcodes          Create a T-code
GET    /api/v1/navigation/tcodes          List all T-codes
GET    /api/v1/navigation/tcodes/{id}     Get by ID
GET    /api/v1/navigation/tcodes/code/{code}  Get by code (e.g., "SU01")
PUT    /api/v1/navigation/tcodes/{id}     Update a T-code
DELETE /api/v1/navigation/tcodes/{id}     Delete a T-code
```

#### Try it yourself

```bash
# Create a T-code
curl -X POST http://localhost:8080/api/v1/navigation/tcodes \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SU01",
    "description": "User Management",
    "module": "Security",
    "route": "/admin/users",
    "icon": "person",
    "active": true
  }'

# Look it up by code
curl http://localhost:8080/api/v1/navigation/tcodes/code/SU01
```

#### Frontend integration

The Angular frontend calls the same API through `TCodeService` in `frontend/src/app/services/tcode.service.ts`:

```typescript
@Injectable({ providedIn: 'root' })
export class TCodeService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/navigation/tcodes';

  getAll(): Observable<TCode[]> {
    return this.http.get<TCode[]>(this.apiUrl);
  }
}
```

---

### 9.3 Data Dictionary (DDIC) and Z-field extensions

The **Data Dictionary** is a metadata store that describes the database schema through the application instead of raw SQL. It is inspired by SAP's SE11 transaction and implements the **ANSI/SPARC three-schema architecture** (External, Conceptual, Internal).

#### Where it lives in the project

```
src/main/java/com/erp/kernel/ddic/
├── entity/
│   ├── TableDefinition.java        # Table metadata
│   ├── TableField.java             # Field metadata within a table
│   ├── Domain.java                 # Value domains (e.g., valid ranges)
│   ├── DataElement.java            # Semantic meaning of a field
│   ├── SearchHelp.java             # Lookup/dropdown definitions
│   └── ExtensionFieldValue.java    # Z-field custom extensions (EAV pattern)
├── dto/                            # Request/response records
├── service/                        # Business logic
├── controller/                     # REST endpoints
├── mapper/                         # Entity ↔ DTO conversion
├── repository/                     # Database access
└── exception/                      # Shared exception classes
```

#### How the layers connect

```
Domain  →  defines allowed values (e.g., length, type)
   ↓
DataElement  →  gives the domain a business meaning (e.g., "Customer Name")
   ↓
TableField  →  places the data element into a specific table column
   ↓
TableDefinition  →  groups fields into a table at a specific schema level
```

#### Three-schema levels

```java
public enum SchemaLevel {
    EXTERNAL,     // How users see the data (views, APIs)
    CONCEPTUAL,   // The logical model (business entities)
    INTERNAL      // Physical storage (database tables)
}
```

#### Z-field extensions — custom fields without modifying the core

Clients can add custom fields prefixed with `Z_` to any table without changing the core schema. This uses the **Entity-Attribute-Value (EAV)** pattern:

```java
@Entity
@Table(name = "ddic_extension_field_value")
public class ExtensionFieldValue extends BaseEntity {

    @Column(name = "table_name", nullable = false, length = 30)
    private String tableName;        // Which table this extends

    @Column(name = "record_id", nullable = false)
    private Long recordId;           // Which row in that table

    @Column(name = "field_name", nullable = false, length = 30)
    private String fieldName;        // Must start with "Z_"

    @Column(name = "field_value", length = 1024)
    private String fieldValue;       // The custom value
}
```

The service validates that all custom field names start with `Z_`:

```java
private static final String EXTENSION_PREFIX = "Z_";

private void validateFieldName(final String fieldName) {
    if (!fieldName.startsWith(EXTENSION_PREFIX)) {
        throw new ValidationException(
            "Extension field name must start with '%s', got '%s'"
                .formatted(EXTENSION_PREFIX, fieldName));
    }
}
```

#### API endpoints

```
POST /api/v1/ddic/tables          Create a table definition
GET  /api/v1/ddic/tables          List tables (optionally filter by schema level)
POST /api/v1/ddic/fields          Create a field within a table
POST /api/v1/ddic/domains         Create a domain
POST /api/v1/ddic/data-elements   Create a data element
POST /api/v1/ddic/search-helps    Create a search help
POST /api/v1/ddic/extensions      Save a Z-field extension value
GET  /api/v1/ddic/extensions      List extension values
```

#### Try it yourself

```bash
# Create a domain
curl -X POST http://localhost:8080/api/v1/ddic/domains \
  -H "Content-Type: application/json" \
  -d '{
    "domainName": "CUSTOMER_NAME",
    "description": "Customer name domain",
    "dataType": "CHAR",
    "length": 100
  }'

# Add a Z-field extension
curl -X POST http://localhost:8080/api/v1/ddic/extensions \
  -H "Content-Type: application/json" \
  -d '{
    "tableName": "CUSTOMER",
    "recordId": 1,
    "fieldName": "Z_LOYALTY_TIER",
    "fieldValue": "GOLD"
  }'
```

---

### 9.4 Spring Profiles — environment switching

A **Spring Profile** is a named configuration set that changes how the application behaves depending on where it runs.

#### How profiles are configured

**`application.yml`** — base configuration (shared across all profiles):

```yaml
spring:
  application:
    name: erp-kernel
  profiles:
    active: local            # ← The default active profile
  jpa:
    hibernate:
      ddl-auto: validate     # Flyway manages schema; JPA only validates
  flyway:
    enabled: true
    locations: classpath:db/migration
```

**`application-local.yml`** — local development (H2 in-memory database):

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:erp_kernel;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
```

**`application-prod.yml`** — production (PostgreSQL, values come from environment variables):

```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/erp_kernel}
    driver-class-name: org.postgresql.Driver
    username: ${DATABASE_USERNAME:erp_kernel}
    password: ${DATABASE_PASSWORD:}
  h2:
    console:
      enabled: false
```

#### How to switch profiles

```bash
# Local (default — just run the app):
./gradlew bootRun

# Production:
./gradlew bootRun --args='--spring.profiles.active=prod'

# Or set via environment variable:
export SPRING_PROFILES_ACTIVE=prod
./gradlew bootRun
```

> **Key takeaway:** You never need to change code to switch environments. Profiles handle everything.

---

### 9.5 Flyway — database migrations

**Flyway** keeps the database schema in sync across all environments. When the application starts, Flyway automatically runs any migration scripts that have not yet been applied.

#### How it works

Migration scripts live in `src/main/resources/db/migration/` and follow a strict naming convention:

```
V{version}__{description}.sql

Examples:
V1__init_schema.sql
V2__ddic_schema.sql
V6__navigation_schema.sql
```

#### Real migration scripts from this project

**`V1__init_schema.sql`** — the first migration, creating the initial table:

```sql
CREATE TABLE IF NOT EXISTS kernel_info (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    info_key      VARCHAR(255) NOT NULL UNIQUE,
    info_value    VARCHAR(1024),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO kernel_info (info_key, info_value)
    VALUES ('version', '0.0.1-SNAPSHOT');
INSERT INTO kernel_info (info_key, info_value)
    VALUES ('phase', 'Phase 1 — Foundation & Infrastructure Setup');
```

**`V6__navigation_schema.sql`** — creating the navigation tables (T-codes, themes, etc.):

```sql
CREATE TABLE nav_tcode (
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code            VARCHAR(20) NOT NULL UNIQUE,
    description     VARCHAR(500) NOT NULL,
    module          VARCHAR(50) NOT NULL,
    route           VARCHAR(500) NOT NULL,
    icon            VARCHAR(100),
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE nav_theme (
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    theme_name      VARCHAR(100) NOT NULL UNIQUE,
    primary_color   VARCHAR(7) NOT NULL,
    secondary_color VARCHAR(7) NOT NULL,
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    is_default      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL
);
```

#### All migration scripts in this project

| Version | File | What it creates |
|---------|------|----------------|
| V1 | `V1__init_schema.sql` | `kernel_info` — framework metadata |
| V2 | `V2__ddic_schema.sql` | Data Dictionary tables (domains, data elements, fields, search helps) |
| V3 | `V3__number_range_schema.sql` | `number_range`, `number_range_interval` |
| V4 | `V4__auth_schema.sql` | Security tables (users, roles, permissions, MFA, WebAuthn, login policies) |
| V5 | `V5__api_schema.sql` | API rate limiting and notification tracking |
| V6 | `V6__navigation_schema.sql` | Navigation tables (T-codes, themes, favourites, preferences, screens) |
| V7 | `V7__resilience_schema.sql` | Backups, disaster recovery, circuit breaker state |

#### How to add a new migration

1. Create a file with the next version number: `V8__my_new_table.sql`.
2. Write the SQL inside it.
3. Restart the application — Flyway runs it automatically.

> **Important:** Never modify an existing migration script once it has been applied. Create a new version instead.

---

### 9.6 JWT — API authentication

**JWT** (JSON Web Token) is how the API verifies that a request comes from an authenticated user. When a user logs in, the server returns a signed token. The client sends this token with every subsequent request.

#### How tokens are generated

The actual implementation in `src/main/java/com/erp/kernel/api/jwt/JwtTokenService.java`:

```java
@Service
public class JwtTokenService {

    private final SecretKey signingKey;
    private final long expirationMinutes;
    private final String issuer;

    public JwtTokenService(final JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode(properties.secretKey()));
        this.expirationMinutes = properties.expirationMinutes();
        this.issuer = properties.issuer();
    }

    public String generateToken(final String username, final List<String> roles) {
        final var now = Instant.now();
        final var expiration = now.plus(Duration.ofMinutes(expirationMinutes));
        return Jwts.builder()
            .subject(username)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("roles", roles)
            .signWith(signingKey)
            .compact();
    }

    public Optional<Claims> validateToken(final String token) {
        try {
            final var claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return Optional.of(claims);
        } catch (final JwtException ex) {
            return Optional.empty();
        }
    }
}
```

#### How the JWT filter protects API endpoints

The `JwtAuthenticationFilter` intercepts every request and checks for a valid `Bearer` token:

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    static final Set<String> PUBLIC_PATHS = Set.of(
        "/api/v1/auth/token",
        "/actuator",
        "/swagger-ui",
        "/v3/api-docs",
        "/ws"
    );

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {

        // Public paths skip authentication
        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the Bearer token from the Authorization header
        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // Validate the token
        final var token = authHeader.substring(BEARER_PREFIX.length());
        final var claimsOpt = jwtTokenService.validateToken(token);
        if (claimsOpt.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // Token is valid — extract user info and continue
        final var username = claimsOpt.get().getSubject();
        final var roles = jwtTokenService.extractRoles(token);
        request.setAttribute("erp.auth.username", username);
        request.setAttribute("erp.auth.roles", roles);
        filterChain.doFilter(request, response);
    }
}
```

#### Configuration in `application.yml`

```yaml
erp:
  api:
    jwt:
      secret-key: ZXJwLWtlcm5lbC1kZXYta2V5...  # Base64-encoded 32-byte key
      expiration-minutes: 60                      # Tokens expire after 1 hour
      issuer: erp-kernel                          # Included in every token
```

#### The authentication flow

```
1. Client sends:  POST /api/v1/auth/token  { username, password }
2. Server returns: { "token": "eyJhbGciOi..." }
3. Client sends:  GET /api/v1/navigation/themes
                  Authorization: Bearer eyJhbGciOi...
4. JwtAuthenticationFilter extracts + validates the token
5. If valid → request proceeds to the controller
   If invalid → 401 Unauthorized response
```

> **Local development note:** In the local profile, the JWT filter is still active, but the Swagger UI and health endpoints are public. Use `/api/v1/auth/token` to get a token for protected endpoints.

---

### 9.7 RBAC — Role-Based Access Control

**RBAC** controls who can do what. Instead of assigning permissions directly to each user, users are assigned **roles**, and roles have **permissions**.

#### Database structure

```
auth_user (id, username, email, ...)
    ↓ one-to-many
auth_user_role (user_id, role_id, assigned_at)
    ↓ many-to-one
auth_role (id, role_name, description, active)
    ↓ many-to-many (via auth_role_permission)
auth_permission (id, permission_name, resource, action)
```

#### Entity classes

**Role** (`src/main/java/com/erp/kernel/security/entity/Role.java`):

```java
@Entity
@Table(name = "auth_role")
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String roleName;           // e.g., "ADMIN", "USER", "VIEWER"

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
```

**Permission** (`src/main/java/com/erp/kernel/security/entity/Permission.java`):

```java
@Entity
@Table(name = "auth_permission")
public class Permission extends BaseEntity {

    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;

    @Column(name = "resource", nullable = false, length = 200)
    private String resource;           // e.g., "USER", "TABLE", "TCODE"

    @Column(name = "action", nullable = false, length = 50)
    private String action;             // e.g., "READ", "WRITE", "DELETE"
}
```

**UserRole** (`src/main/java/com/erp/kernel/security/entity/UserRole.java`):

```java
@Entity
@Table(name = "auth_user_role")
public class UserRole {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
}
```

#### How it works in practice

```
Example:

User "alice"
   └── Role "ADMIN"
         ├── Permission: USER:READ
         ├── Permission: USER:WRITE
         ├── Permission: TABLE:READ
         └── Permission: TABLE:WRITE

User "bob"
   └── Role "VIEWER"
         └── Permission: TABLE:READ
```

Alice can read and write users and tables. Bob can only read tables.

#### API endpoints

```
POST /api/v1/security/users        Create a user
GET  /api/v1/security/users        List users
POST /api/v1/security/roles        Create a role
GET  /api/v1/security/roles        List roles
```

---

### 9.8 Plugins and verticals

A **Plugin** is a self-contained module that extends the ERP Kernel without modifying the core code. Industry-specific applications (called **verticals**) — such as Healthcare, Manufacturing, or Retail — are built as plugins.

#### Plugin interface and lifecycle

Every plugin implements the `Plugin` interface (`src/main/java/com/erp/kernel/plugin/Plugin.java`):

```java
public interface Plugin {
    String getId();
    String getName();
    String getVersion();
    PluginState getState();
    List<String> getDependencies();

    void initialize(PluginContext context);   // Set up resources
    void start();                             // Make functionality available
    void stop();                              // Suspend functionality
    void destroy();                           // Release all resources
}
```

Plugins go through a managed lifecycle:

```
CREATED  →  INITIALIZED  →  STARTED  →  STOPPED  →  DESTROYED
             (initialize)    (start)      (stop)      (destroy)
```

#### Plugin registry (thread-safe)

The `PluginRegistry` manages all registered plugins (`src/main/java/com/erp/kernel/plugin/PluginRegistry.java`):

```java
@Component
public class PluginRegistry {

    private final ConcurrentMap<String, Plugin> plugins = new ConcurrentHashMap<>();

    public void register(final Plugin plugin) {
        final var previous = plugins.putIfAbsent(plugin.getId(), plugin);
        if (previous != null) {
            throw new IllegalArgumentException(
                "Plugin already registered: " + plugin.getId());
        }
    }

    public Optional<Plugin> findById(final String id) {
        return Optional.ofNullable(plugins.get(id));
    }

    public Collection<Plugin> getAll() {
        return Collections.unmodifiableCollection(plugins.values());
    }
}
```

#### Industry verticals

Verticals are defined by the `VerticalType` enum (`src/main/java/com/erp/kernel/vertical/VerticalType.java`):

```java
public enum VerticalType {
    HEALTHCARE("Healthcare & One Health"),
    MANUFACTURING("Manufacturing & Production"),
    RETAIL("Retail & Distribution"),
    FINANCE("Financial Services"),
    EDUCATION("Education & Academic");
}
```

Each vertical registers itself by declaring which framework capabilities it requires. The kernel validates that all requirements are met before the vertical is activated.

---

### 9.9 Number Ranges

**Number Ranges** provide automatic, sequential number generation for business documents (invoices, orders, patients, etc.) — inspired by SAP's SNRO transaction.

#### How it works

A **Number Range** is an object (e.g., `DOCNR` for document numbers) that contains one or more **intervals** (e.g., `01` = 0000000001–9999999999). The service increments a counter each time a new number is requested.

```java
@Entity
@Table(name = "number_range")
public class NumberRange extends BaseEntity {

    @Column(name = "range_object", nullable = false, unique = true, length = 30)
    private String rangeObject;    // e.g., "DOCNR", "ORDNR"

    @Column(name = "is_buffered", nullable = false)
    private boolean buffered;      // Buffered = faster, Non-buffered = gap-free

    @Column(name = "buffer_size")
    private Integer bufferSize;    // How many numbers to pre-allocate
}
```

#### Number assignment in the service

```java
@Transactional
public NextNumberResponse getNextNumber(
        final String rangeObject, final String subObject) {

    final var numberRange = numberRangeRepository.findByRangeObject(rangeObject)
        .orElseThrow(() -> new EntityNotFoundException(
            "Number range '%s' not found".formatted(rangeObject)));

    final var interval = intervalRepository
        .findByNumberRangeAndSubObject(numberRange, subObject)
        .orElseThrow(() -> new EntityNotFoundException(
            "Interval not found for range '%s'".formatted(rangeObject)));

    final var current = Long.parseLong(interval.getCurrentNumber());
    final var upper = Long.parseLong(interval.getToNumber());

    if (current >= upper) {
        throw new ValidationException(
            "Number range interval '%s/%s' is exhausted"
                .formatted(rangeObject, subObject));
    }

    final var next = current + 1;
    interval.setCurrentNumber(String.valueOf(next));
    intervalRepository.save(interval);

    return new NextNumberResponse(rangeObject, subObject, String.valueOf(next));
}
```

| Type | Buffered | Gap-free? | Use case |
|------|----------|-----------|----------|
| **Buffered** | Yes — pre-allocates a block of numbers | No — gaps possible if the app restarts | High-throughput (orders, logs) |
| **Non-buffered** | No — one at a time from DB | Yes — no gaps | Regulatory (invoices, audit numbers) |

---

### 9.10 Encryption (AES-256-GCM)

Sensitive data (patient records, financial data) is encrypted at the application level before being stored in the database, using **AES-256 with GCM mode**.

#### How it works in the project

```java
@Service
public class EncryptionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    public String encrypt(final String plaintext) {
        // 1. Generate a random 12-byte IV
        final var iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);

        // 2. Encrypt using AES-256-GCM
        final var cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,
            new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        final var ciphertext = cipher.doFinal(
            plaintext.getBytes(StandardCharsets.UTF_8));

        // 3. Combine IV + ciphertext and Base64-encode
        final var combined = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(final String encryptedText) {
        // Reverse: decode → extract IV → decrypt
        final var combined = Base64.getDecoder().decode(encryptedText);
        final var iv = Arrays.copyOfRange(combined, 0, GCM_IV_LENGTH);
        final var ciphertext = Arrays.copyOfRange(combined, GCM_IV_LENGTH, combined.length);

        final var cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,
            new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    }
}
```

The encryption key is configured per environment:

```yaml
# application-local.yml (development key — NEVER use in production)
erp:
  encryption:
    secret-key: MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=

# application-prod.yml (from environment variable)
erp:
  encryption:
    secret-key: ${ENCRYPTION_SECRET_KEY:}
```

---

### 9.11 System Variables (SY-*)

**System Variables** provide globally available, read-only values — inspired by SAP's `SY-*` fields. They include the current date, time, username, and system identifier.

#### Available system variables

```java
public enum SystemVariable {
    SY_DATUM("SY-DATUM", "Current date",           "DATS"),
    SY_UZEIT("SY-UZEIT", "Current time",           "TIMS"),
    SY_UNAME("SY-UNAME", "Current user name",      "CHAR"),
    SY_LANGU("SY-LANGU", "Logon language",          "LANG"),
    SY_MANDT("SY-MANDT", "Client number",           "CLNT"),
    SY_HOST ("SY-HOST",  "Application server host", "CHAR"),
    SY_SYSID("SY-SYSID", "System identifier",       "CHAR"),
    SY_PAGNO("SY-PAGNO", "Current page number",     "INT4"),
    SY_SUBRC("SY-SUBRC", "Return code",             "INT4"),
    SY_INDEX("SY-INDEX", "Loop index",              "INT4"),
    SY_DBCNT("SY-DBCNT", "Database row count",      "INT4");
}
```

#### How values are resolved at runtime

```java
@Service
public class DefaultSystemVariableProvider implements SystemVariableProvider {

    @Override
    public String getValue(final SystemVariable variable) {
        return switch (variable) {
            case SY_DATUM -> LocalDate.now().format(DATS_FORMATTER);    // "20260311"
            case SY_UZEIT -> LocalTime.now().format(TIMS_FORMATTER);    // "143022"
            case SY_UNAME -> System.getProperty("user.name");           // Current OS user
            case SY_LANGU -> "E";                                       // English
            case SY_MANDT -> "000";                                     // Default client
            case SY_HOST  -> InetAddress.getLocalHost().getHostName();
            case SY_SYSID -> "ERP";
            case SY_PAGNO -> "0";
            case SY_SUBRC -> "0";
            case SY_INDEX -> "0";
            case SY_DBCNT -> "0";
        };
    }
}
```

#### Try it yourself

```bash
curl http://localhost:8080/api/v1/system/variables
```

---

### 9.12 Business Logic — Rules Engine

The **Rules Engine** lets you register validation rules and substitution rules that are evaluated automatically during business processing.

```java
@Component
public class RuleEngine {

    // Register validation rules
    public void registerValidationRule(
            final BusinessRule<RuleContext, ValidationResult> rule) {
        validationRules.add(rule);
    }

    // Execute all validation rules against a context
    public ValidationResult validate(final RuleContext context) {
        final var allMessages = new ArrayList<String>();
        for (final var rule : validationRules) {
            if (rule.isActive()) {
                final var result = rule.evaluate(context);
                if (!result.valid()) {
                    allMessages.addAll(result.messages());
                }
            }
        }
        return allMessages.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure(allMessages);
    }

    // Execute all substitution rules (auto-fill values)
    public SubstitutionResult substitute(final RuleContext context) {
        final var combined = new HashMap<String, Object>();
        for (final var rule : substitutionRules) {
            if (rule.isActive()) {
                final var result = rule.evaluate(context);
                if (result.applied()) {
                    combined.putAll(result.substitutions());
                }
            }
        }
        return combined.isEmpty()
            ? SubstitutionResult.none()
            : SubstitutionResult.of(combined);
    }
}
```

| Rule type | Purpose | Example |
|-----------|---------|---------|
| **Validation** | Checks if data is valid before saving | "Order amount must be > 0" |
| **Substitution** | Automatically fills in field values | "Default currency = USD for US customers" |

---

### 9.13 Concept reference table

| Concept | Package | Key files | API endpoint |
|---------|---------|-----------|-------------|
| Three-layer pattern | `navigation/` | `ThemeController`, `ThemeService`, `ThemeRepository` | `/api/v1/navigation/themes` |
| T-codes | `navigation/` | `TCode`, `TCodeService`, `TCodeController` | `/api/v1/navigation/tcodes` |
| Data Dictionary | `ddic/` | `TableDefinition`, `Domain`, `ExtensionFieldValue` | `/api/v1/ddic/tables` |
| Spring Profiles | `resources/` | `application.yml`, `application-local.yml`, `application-prod.yml` | N/A |
| Flyway | `resources/db/migration/` | `V1__init_schema.sql` through `V7__resilience_schema.sql` | N/A |
| JWT | `api/jwt/` | `JwtTokenService`, `JwtAuthenticationFilter` | `POST /api/v1/auth/token` |
| RBAC | `security/` | `Role`, `Permission`, `UserRole` | `/api/v1/security/roles` |
| Plugins | `plugin/` | `Plugin`, `PluginRegistry` | `/api/v1/plugins` |
| Verticals | `vertical/` | `VerticalType`, `VerticalDescriptor` | `/api/v1/verticals` |
| Number Ranges | `numberrange/` | `NumberRange`, `NumberRangeService` | `/api/v1/number-ranges` |
| Encryption | `encryption/` | `EncryptionService` | N/A (used internally) |
| System Variables | `sysvar/` | `SystemVariable`, `DefaultSystemVariableProvider` | `/api/v1/system/variables` |
| Business Logic | `businesslogic/` | `RuleEngine`, `BusinessRule` | N/A (used internally) |

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
