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
      - [9.3.1 What is a Data Dictionary?](#931-what-is-a-data-dictionary)
      - [9.3.2 The building blocks](#932-the-building-blocks--how-they-fit-together)
      - [9.3.3 Three-schema architecture](#933-the-three-schema-architecture)
      - [9.3.4 Project file structure](#934-where-it-lives-in-the-project)
      - [9.3.5 Database schema (Flyway)](#935-the-database-schema-flyway-migration)
      - [9.3.6 Entity classes](#936-the-real-entity-classes)
      - [9.3.7 DTOs](#937-the-request-and-response-dtos)
      - [9.3.8 Service layer](#938-the-service-layer--business-logic-and-validation)
      - [9.3.9 REST API endpoints](#939-the-rest-api-endpoints)
      - [9.3.10 Walkthrough: build a CUSTOMERS table](#9310-complete-walkthrough--building-a-customers-table-from-scratch)
      - [9.3.11 Z-fields explained](#9311-what-are-z-fields-and-why-do-they-exist)
      - [9.3.12 Adding a new DDIC feature](#9312-how-to-add-a-new-ddic-feature-as-a-developer)
      - [9.3.13 Quick reference](#9313-ddic-quick-reference)
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

> This is the most important concept unique to this project. Read this section carefully if you are new — it explains **what DDIC is**, **why it exists**, **how every piece connects**, and walks you through a **complete end-to-end example** using the real APIs.

---

#### 9.3.1 What is a Data Dictionary?

Imagine you are building an ERP system that manages customers, orders, invoices, materials, and employees. Each of these has database tables with columns. Normally, you would write SQL like:

```sql
CREATE TABLE customers (
    customer_name VARCHAR(100),
    country_code  VARCHAR(3),
    ...
);
```

The problem with this approach in a large ERP system:

1. **No reuse** — If `customer_name` and `supplier_name` both need the same rules (max 100 characters, type CHAR), you define those rules separately in each table. Change one and you must find and change all the others.
2. **No labels** — The database column `country_code` does not know that it should display as "Country", "Ctry", or "Country Code" on different screen sizes.
3. **No extensions** — A hospital client needs a `BLOOD_TYPE` column on the patient table, but the core ERP code should not be modified for one client.

The **Data Dictionary (DDIC)** solves all three problems by managing the database schema **as data within the application itself**. Instead of hardcoding table structures in SQL, you define reusable building blocks — **Domains**, **Data Elements**, **Table Definitions**, **Table Fields**, **Search Helps**, and **Extension Fields** — through REST APIs.

> **In simple terms:** The DDIC is a "database that describes your database". It is inspired by SAP's SE11 transaction and implements the ANSI/SPARC three-schema architecture.

---

#### 9.3.2 The building blocks — how they fit together

The DDIC has six building blocks, and they connect in a specific order. Think of them as Lego bricks — you build from the bottom up:

```
┌──────────────────────────────────────────────────────────────────┐
│                     TABLE DEFINITION                             │
│    (e.g., "CUSTOMERS" — a table at a specific schema level)      │
│                                                                  │
│    ┌──────────────────────────────────────────────────────────┐   │
│    │  TABLE FIELD            TABLE FIELD            TABLE FIELD│  │
│    │  (e.g., "CUST_NAME")   (e.g., "COUNTRY")     (Z_LOYALTY) │  │
│    │       │                      │                     │      │  │
│    │       ▼                      ▼                     ▼      │  │
│    │  DATA ELEMENT          DATA ELEMENT           DATA ELEMENT│  │
│    │  (labels, desc)        (labels, desc)         (labels)    │  │
│    │       │                      │                     │      │  │
│    │       ▼                      ▼                     ▼      │  │
│    │    DOMAIN                 DOMAIN                DOMAIN     │  │
│    │  (CHAR, 100)           (CHAR, 3)             (CHAR, 20)   │  │
│    └──────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────┘

SEARCH HELP  →  links to a TABLE DEFINITION for dropdown lookups

EXTENSION FIELD VALUE  →  stores custom "Z_" field values (EAV pattern)
```

Here is what each building block does:

| Building block | What it defines | Real-world analogy | Example |
|---|---|---|---|
| **Domain** | Technical rules: data type, max length, decimal places | A specification sheet — "this must be a string of at most 100 characters" | `CHAR_100` = type CHAR, length 100 |
| **Data Element** | Business meaning: labels for different screen sizes, description. References one Domain. | A label maker — "this field should display as 'Customer Name' on desktop and 'Name' on mobile" | `CUST_NAME_EL` with labels "Name" / "Cust Name" / "Customer Name" |
| **Table Definition** | A logical table at a specific schema level (External, Conceptual, or Internal) | A table blueprint — "this is the CUSTOMERS table and it exists at the CONCEPTUAL level" | `CUSTOMERS` at schema level `CONCEPTUAL` |
| **Table Field** | A column within a table. References one Data Element and one Table Definition. | A column in a spreadsheet — "column 1 is CUST_NAME, column 2 is COUNTRY" | `CUST_NAME` at position 1, key=false, nullable=false |
| **Search Help** | A lookup mechanism linked to a Table Definition for dropdown suggestions | A dropdown list — "when the user clicks the Country field, show a list of valid countries from the COUNTRIES table" | `SH_COUNTRY` linked to `COUNTRIES` table |
| **Extension Field Value** | A custom value for a "Z_" field on any record in any table (EAV pattern) | A sticky note on a record — "for customer #42, add a 'Z_LOYALTY_TIER' field with value 'GOLD'" | `Z_LOYALTY_TIER` = "GOLD" on CUSTOMERS record 42 |

---

#### 9.3.3 The three-schema architecture

Every Table Definition has a **schema level** — this comes from the ANSI/SPARC standard, which separates a database into three views:

```java
// File: src/main/java/com/erp/kernel/ddic/model/SchemaLevel.java

public enum SchemaLevel {
    /** The user or application view of data. */
    EXTERNAL,

    /** The logical structure of the entire database. */
    CONCEPTUAL,

    /** The physical storage representation. */
    INTERNAL
}
```

| Level | What it means | Example |
|---|---|---|
| **EXTERNAL** | How a specific user or application sees the data. Like a database view — shows only relevant columns. | A "Customer Summary" view showing just name and country |
| **CONCEPTUAL** | The complete logical model of the business entity. This is the "truth" — all fields an entity actually has. | The full `CUSTOMERS` table with all 20 columns |
| **INTERNAL** | How data is physically stored (indexes, partitions, storage details). | Physical storage optimisations for the CUSTOMERS table |

> **For freshers:** You will almost always work with `CONCEPTUAL` level tables. The other levels are used for advanced data modelling.

---

#### 9.3.4 Where it lives in the project

```
src/main/java/com/erp/kernel/ddic/
├── entity/                              # JPA entities — the database tables
│   ├── BaseEntity.java                  # Shared: id, createdAt, updatedAt
│   ├── Domain.java                      # Technical rules (type, length, decimals)
│   ├── DataElement.java                 # Business meaning (labels, references a Domain)
│   ├── TableDefinition.java             # Logical table (name, schema level)
│   ├── TableField.java                  # Column (references a Table + Data Element)
│   ├── SearchHelp.java                  # Lookup mechanism (references a Table)
│   └── ExtensionFieldValue.java         # Custom Z-field values (EAV pattern)
├── dto/                                 # Request/response records
│   ├── CreateDomainRequest.java         # What you POST to create a domain
│   ├── DomainDto.java                   # What the API returns after creating
│   ├── CreateDataElementRequest.java
│   ├── DataElementDto.java
│   ├── CreateTableDefinitionRequest.java
│   ├── TableDefinitionDto.java
│   ├── CreateTableFieldRequest.java
│   ├── TableFieldDto.java
│   ├── CreateExtensionFieldValueRequest.java
│   ├── ExtensionFieldValueDto.java
│   ├── CreateSearchHelpRequest.java
│   └── SearchHelpDto.java
├── service/                             # Business logic (validation, caching)
│   ├── DomainService.java
│   ├── DataElementService.java
│   ├── TableDefinitionService.java
│   ├── TableFieldService.java
│   ├── ExtensionFieldService.java
│   └── SearchHelpService.java
├── controller/                          # REST API endpoints
│   ├── DomainController.java            # /api/v1/ddic/domains
│   ├── DataElementController.java       # /api/v1/ddic/data-elements
│   ├── TableDefinitionController.java   # /api/v1/ddic/tables
│   ├── TableFieldController.java        # /api/v1/ddic/fields
│   ├── ExtensionFieldController.java    # /api/v1/ddic/extensions
│   └── SearchHelpController.java        # /api/v1/ddic/search-helps
├── mapper/                              # Entity ↔ DTO conversion
├── repository/                          # Spring Data JPA repositories
├── model/
│   └── SchemaLevel.java                 # EXTERNAL, CONCEPTUAL, INTERNAL enum
└── exception/                           # Shared exception classes (used project-wide)
    ├── EntityNotFoundException.java
    ├── DuplicateEntityException.java
    ├── ValidationException.java
    └── GlobalExceptionHandler.java
```

---

#### 9.3.5 The database schema (Flyway migration)

All DDIC tables are created by `src/main/resources/db/migration/V2__ddic_schema.sql`. This runs automatically when the application starts. Here is the complete migration:

```sql
-- Domains define technical attributes (data type, length, value range)
CREATE TABLE ddic_domain (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    domain_name     VARCHAR(30) NOT NULL UNIQUE,
    data_type       VARCHAR(20) NOT NULL,
    max_length      INTEGER,
    decimal_places  INTEGER DEFAULT 0,
    description     VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Data Elements define semantic attributes and reference a domain
CREATE TABLE ddic_data_element (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    element_name    VARCHAR(30) NOT NULL UNIQUE,
    domain_id       BIGINT NOT NULL REFERENCES ddic_domain(id),
    short_label     VARCHAR(10),
    medium_label    VARCHAR(20),
    long_label      VARCHAR(40),
    description     VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table Definitions with schema level (ANSI/SPARC three-schema model)
CREATE TABLE ddic_table_definition (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_name          VARCHAR(30) NOT NULL UNIQUE,
    schema_level        VARCHAR(20) NOT NULL,
    description         VARCHAR(255),
    is_client_specific  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_schema_level CHECK (schema_level IN ('EXTERNAL', 'CONCEPTUAL', 'INTERNAL'))
);

-- Table Fields (columns) referencing data elements
CREATE TABLE ddic_table_field (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_definition_id   BIGINT NOT NULL REFERENCES ddic_table_definition(id),
    field_name            VARCHAR(30) NOT NULL,
    data_element_id       BIGINT NOT NULL REFERENCES ddic_data_element(id),
    position              INTEGER NOT NULL,
    is_key                BOOLEAN NOT NULL DEFAULT FALSE,
    is_nullable           BOOLEAN NOT NULL DEFAULT TRUE,
    is_extension          BOOLEAN NOT NULL DEFAULT FALSE,
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_table_field UNIQUE (table_definition_id, field_name)
);

-- Search Helps for field value lookups
CREATE TABLE ddic_search_help (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    search_help_name      VARCHAR(30) NOT NULL UNIQUE,
    table_definition_id   BIGINT NOT NULL REFERENCES ddic_table_definition(id),
    description           VARCHAR(255),
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Extension field values for client-specific "Z" fields (EAV pattern)
CREATE TABLE ddic_extension_field_value (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_name      VARCHAR(30) NOT NULL,
    record_id       BIGINT NOT NULL,
    field_name      VARCHAR(30) NOT NULL,
    field_value     VARCHAR(1024),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_extension_value UNIQUE (table_name, record_id, field_name)
);
```

---

#### 9.3.6 The real entity classes

Each DDIC building block is a JPA entity. Here are the key ones with annotations explained:

**Domain** — the foundation (`src/main/java/com/erp/kernel/ddic/entity/Domain.java`):

```java
@Entity
@Table(name = "ddic_domain")
public class Domain extends BaseEntity {

    @Column(name = "domain_name", nullable = false, unique = true, length = 30)
    private String domainName;       // e.g., "CHAR_100", "NUMC_10", "DEC_13_2"

    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType;         // CHAR, NUMC, DEC, INT, DATS, TIMS

    @Column(name = "max_length")
    private Integer maxLength;       // e.g., 100 for a 100-character string

    @Column(name = "decimal_places")
    private Integer decimalPlaces;   // e.g., 2 for prices like 19.99

    @Column(name = "description")
    private String description;
}
```

**Data Element** — adds business meaning (`src/main/java/com/erp/kernel/ddic/entity/DataElement.java`):

```java
@Entity
@Table(name = "ddic_data_element")
public class DataElement extends BaseEntity {

    @Column(name = "element_name", nullable = false, unique = true, length = 30)
    private String elementName;      // e.g., "CUST_NAME_EL"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;           // Links to the referenced domain → inherits type + length

    @Column(name = "short_label", length = 10)
    private String shortLabel;       // "Name"       — for narrow columns / mobile

    @Column(name = "medium_label", length = 20)
    private String mediumLabel;      // "Cust Name"  — for medium-width columns

    @Column(name = "long_label", length = 40)
    private String longLabel;        // "Customer Name" — for full-width displays

    @Column(name = "description")
    private String description;      // "Full legal name of the customer"
}
```

**Table Definition** — describes a table (`src/main/java/com/erp/kernel/ddic/entity/TableDefinition.java`):

```java
@Entity
@Table(name = "ddic_table_definition")
public class TableDefinition extends BaseEntity {

    @Column(name = "table_name", nullable = false, unique = true, length = 30)
    private String tableName;        // e.g., "CUSTOMERS"

    @Enumerated(EnumType.STRING)
    @Column(name = "schema_level", nullable = false, length = 20)
    private SchemaLevel schemaLevel; // EXTERNAL, CONCEPTUAL, or INTERNAL

    @Column(name = "description")
    private String description;      // "Master data for all customers"

    @Column(name = "is_client_specific", nullable = false)
    private boolean clientSpecific;  // true = data is isolated per client/tenant
}
```

**Table Field** — a column within a table (`src/main/java/com/erp/kernel/ddic/entity/TableField.java`):

```java
@Entity
@Table(name = "ddic_table_field",
       uniqueConstraints = @UniqueConstraint(columnNames = {"table_definition_id", "field_name"}))
public class TableField extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_definition_id", nullable = false)
    private TableDefinition tableDefinition;  // Which table this field belongs to

    @Column(name = "field_name", nullable = false, length = 30)
    private String fieldName;                 // e.g., "CUST_NAME" or "Z_LOYALTY_TIER"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_element_id", nullable = false)
    private DataElement dataElement;          // Links to data element → inherits labels

    @Column(name = "position", nullable = false)
    private int position;                     // Column order: 1, 2, 3, ...

    @Column(name = "is_key", nullable = false)
    private boolean key;                      // true = part of the primary key

    @Column(name = "is_nullable", nullable = false)
    private boolean nullable;                 // true = allows NULL values

    @Column(name = "is_extension", nullable = false)
    private boolean extension;                // true = this is a Z-field (client extension)
}
```

**BaseEntity** — shared fields for all entities (`src/main/java/com/erp/kernel/ddic/entity/BaseEntity.java`):

```java
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        final var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

> Every DDIC entity inherits `id`, `createdAt`, and `updatedAt` from `BaseEntity`. Timestamps are set automatically — you never need to set them manually.

---

#### 9.3.7 The request and response DTOs

This project uses Java **records** for DTOs — they are immutable data containers. Here are the key ones:

**Creating a Domain** — what you send in the POST body:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/CreateDomainRequest.java
public record CreateDomainRequest(
    @NotBlank @Size(max = 30) String domainName,     // Required, max 30 chars
    @NotBlank @Size(max = 20) String dataType,        // Required: CHAR, NUMC, DEC, INT...
    Integer maxLength,                                // Optional: e.g., 100
    Integer decimalPlaces,                            // Optional: e.g., 2
    @Size(max = 255) String description               // Optional
) {}
```

**Domain response** — what the API returns:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/DomainDto.java
public record DomainDto(
    Long id,                 // Auto-generated by the database
    String domainName,
    String dataType,
    Integer maxLength,
    Integer decimalPlaces,
    String description,
    Instant createdAt,       // Set automatically
    Instant updatedAt        // Set automatically
) {}
```

**Creating a Data Element** — references a Domain by ID:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/CreateDataElementRequest.java
public record CreateDataElementRequest(
    @NotBlank @Size(max = 30) String elementName,
    @NotNull Long domainId,                          // Must reference an existing Domain
    @Size(max = 10) String shortLabel,               // e.g., "Name"
    @Size(max = 20) String mediumLabel,              // e.g., "Cust Name"
    @Size(max = 40) String longLabel,                // e.g., "Customer Name"
    @Size(max = 255) String description
) {}
```

**Creating a Table Definition**:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/CreateTableDefinitionRequest.java
public record CreateTableDefinitionRequest(
    @NotBlank @Size(max = 30) String tableName,
    @NotNull SchemaLevel schemaLevel,                // EXTERNAL, CONCEPTUAL, or INTERNAL
    @Size(max = 255) String description,
    boolean clientSpecific                           // true = tenant-isolated data
) {}
```

**Creating a Table Field** — references both a Table Definition and a Data Element:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/CreateTableFieldRequest.java
public record CreateTableFieldRequest(
    @NotNull Long tableDefinitionId,                 // Which table this field belongs to
    @NotBlank @Size(max = 30) String fieldName,      // Column name (Z_ prefix for extensions)
    @NotNull Long dataElementId,                     // Which data element provides the labels
    @Min(1) int position,                            // Column order (1, 2, 3, ...)
    boolean key,                                     // Is this a primary key field?
    boolean nullable,                                // Can this field be NULL?
    boolean extension                                // Is this a Z-field (client extension)?
) {}
```

**Creating a Z-field Extension Value**:

```java
// File: src/main/java/com/erp/kernel/ddic/dto/CreateExtensionFieldValueRequest.java
public record CreateExtensionFieldValueRequest(
    @NotBlank @Size(max = 30) String tableName,      // Which table the record belongs to
    @NotNull Long recordId,                          // Which row in that table
    @NotBlank @Size(max = 30) String fieldName,      // Must start with "Z_"
    @Size(max = 1024) String fieldValue              // The custom value to store
) {}
```

---

#### 9.3.8 The service layer — business logic and validation

Each DDIC component has a dedicated service. They all follow the same pattern: validate input → check for duplicates → save → return DTO. Here are the most important ones:

**DomainService** — creating a domain (`src/main/java/com/erp/kernel/ddic/service/DomainService.java`):

```java
@Service
@Transactional(readOnly = true)
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainService(final DomainRepository domainRepository) {
        this.domainRepository = Objects.requireNonNull(domainRepository,
            "domainRepository must not be null");
    }

    @Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public DomainDto create(final CreateDomainRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        // Check for duplicate names
        if (domainRepository.existsByDomainName(request.domainName())) {
            throw new DuplicateEntityException(
                "Domain with name '%s' already exists".formatted(request.domainName()));
        }
        final var entity = DomainMapper.toEntity(request);
        final var saved = domainRepository.save(entity);
        return DomainMapper.toDto(saved);
    }

    @Cacheable(value = "domains", key = "#id")
    public DomainDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return domainRepository.findById(id)
            .map(DomainMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(
                "Domain with id %d not found".formatted(id)));
    }

    @Cacheable(value = "domains", key = "'all'")
    public List<DomainDto> findAll() {
        return domainRepository.findAll().stream()
            .map(DomainMapper::toDto)
            .toList();
    }
}
```

**DataElementService** — creating a data element that references a domain (`src/main/java/com/erp/kernel/ddic/service/DataElementService.java`):

```java
@Service
@Transactional(readOnly = true)
public class DataElementService {

    private final DataElementRepository dataElementRepository;
    private final DomainRepository domainRepository;

    @Transactional
    @CacheEvict(value = "dataElements", allEntries = true)
    public DataElementDto create(final CreateDataElementRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (dataElementRepository.existsByElementName(request.elementName())) {
            throw new DuplicateEntityException(
                "Data element with name '%s' already exists".formatted(request.elementName()));
        }
        // Look up the referenced domain — fail if it doesn't exist
        final var domain = domainRepository.findById(request.domainId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Domain with id %d not found".formatted(request.domainId())));
        final var entity = DataElementMapper.toEntity(request, domain);
        final var saved = dataElementRepository.save(entity);
        return DataElementMapper.toDto(saved);
    }
}
```

**TableFieldService** — the Z-field validation logic (`src/main/java/com/erp/kernel/ddic/service/TableFieldService.java`):

```java
@Service
@Transactional(readOnly = true)
public class TableFieldService {

    private static final String EXTENSION_PREFIX = "Z_";

    @Transactional
    public TableFieldDto create(final CreateTableFieldRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        // KEY VALIDATION: if this is marked as an extension field,
        // the field name MUST start with "Z_"
        validateExtensionFieldName(request);

        final var tableDefinition = tableDefinitionRepository.findById(request.tableDefinitionId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Table definition with id %d not found".formatted(request.tableDefinitionId())));
        final var dataElement = dataElementRepository.findById(request.dataElementId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Data element with id %d not found".formatted(request.dataElementId())));

        // No duplicate field names in the same table
        if (tableFieldRepository.existsByTableDefinitionIdAndFieldName(
                request.tableDefinitionId(), request.fieldName())) {
            throw new DuplicateEntityException(
                "Field '%s' already exists in table definition %d"
                    .formatted(request.fieldName(), request.tableDefinitionId()));
        }

        final var entity = TableFieldMapper.toEntity(request, tableDefinition, dataElement);
        final var saved = tableFieldRepository.save(entity);
        return TableFieldMapper.toDto(saved);
    }

    // You can query only extension fields for a table
    public List<TableFieldDto> findExtensionFields(final Long tableDefinitionId) {
        return tableFieldRepository.findByTableDefinitionIdAndExtensionTrue(tableDefinitionId)
            .stream()
            .map(TableFieldMapper::toDto)
            .toList();
    }

    private void validateExtensionFieldName(final CreateTableFieldRequest request) {
        if (request.extension() && !request.fieldName().startsWith(EXTENSION_PREFIX)) {
            throw new ValidationException(
                "Extension field name must start with '%s', got '%s'"
                    .formatted(EXTENSION_PREFIX, request.fieldName()));
        }
    }
}
```

**ExtensionFieldService** — storing custom Z-field values (`src/main/java/com/erp/kernel/ddic/service/ExtensionFieldService.java`):

```java
@Service
@Transactional(readOnly = true)
public class ExtensionFieldService {

    private static final String EXTENSION_PREFIX = "Z_";

    @Transactional
    public ExtensionFieldValueDto save(final CreateExtensionFieldValueRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        validateFieldName(request.fieldName());

        // Upsert: update if the value already exists, create if it does not
        final var entity = extensionFieldValueRepository
            .findByTableNameAndRecordIdAndFieldName(
                request.tableName(), request.recordId(), request.fieldName())
            .map(existing -> {
                ExtensionFieldValueMapper.updateEntity(existing, request);
                return existing;
            })
            .orElseGet(() -> ExtensionFieldValueMapper.toEntity(request));

        final var saved = extensionFieldValueRepository.save(entity);
        return ExtensionFieldValueMapper.toDto(saved);
    }

    public List<ExtensionFieldValueDto> findByRecord(final String tableName, final Long recordId) {
        return extensionFieldValueRepository.findByTableNameAndRecordId(tableName, recordId)
            .stream()
            .map(ExtensionFieldValueMapper::toDto)
            .toList();
    }

    private void validateFieldName(final String fieldName) {
        if (!fieldName.startsWith(EXTENSION_PREFIX)) {
            throw new ValidationException(
                "Extension field name must start with '%s', got '%s'"
                    .formatted(EXTENSION_PREFIX, fieldName));
        }
    }
}
```

---

#### 9.3.9 The REST API endpoints

Each DDIC component has full CRUD endpoints. Here is the complete list:

| Resource | Method | Endpoint | What it does |
|---|---|---|---|
| **Domains** | POST | `/api/v1/ddic/domains` | Create a new domain |
| | GET | `/api/v1/ddic/domains` | List all domains |
| | GET | `/api/v1/ddic/domains/{id}` | Get a domain by ID |
| | PUT | `/api/v1/ddic/domains/{id}` | Update a domain |
| | DELETE | `/api/v1/ddic/domains/{id}` | Delete a domain |
| **Data Elements** | POST | `/api/v1/ddic/data-elements` | Create a data element (references a domain) |
| | GET | `/api/v1/ddic/data-elements` | List all data elements |
| | GET | `/api/v1/ddic/data-elements/{id}` | Get a data element by ID |
| | PUT | `/api/v1/ddic/data-elements/{id}` | Update a data element |
| | DELETE | `/api/v1/ddic/data-elements/{id}` | Delete a data element |
| **Table Definitions** | POST | `/api/v1/ddic/tables` | Create a table definition |
| | GET | `/api/v1/ddic/tables` | List all tables (optional `?schemaLevel=CONCEPTUAL` filter) |
| | GET | `/api/v1/ddic/tables/{id}` | Get a table by ID |
| | PUT | `/api/v1/ddic/tables/{id}` | Update a table |
| | DELETE | `/api/v1/ddic/tables/{id}` | Delete a table |
| **Table Fields** | POST | `/api/v1/ddic/fields` | Create a field in a table |
| | GET | `/api/v1/ddic/fields?tableDefinitionId=1` | List fields for a table |
| | GET | `/api/v1/ddic/fields?tableDefinitionId=1&extensionsOnly=true` | List only Z-fields |
| | GET | `/api/v1/ddic/fields/{id}` | Get a field by ID |
| | DELETE | `/api/v1/ddic/fields/{id}` | Delete a field |
| **Search Helps** | POST | `/api/v1/ddic/search-helps` | Create a search help |
| | GET | `/api/v1/ddic/search-helps` | List all search helps |
| | GET | `/api/v1/ddic/search-helps/{id}` | Get a search help by ID |
| | PUT | `/api/v1/ddic/search-helps/{id}` | Update a search help |
| | DELETE | `/api/v1/ddic/search-helps/{id}` | Delete a search help |
| **Extension Values** | POST | `/api/v1/ddic/extensions` | Create or update a Z-field value |
| | GET | `/api/v1/ddic/extensions?tableName=X&recordId=1` | List Z-field values for a record |
| | DELETE | `/api/v1/ddic/extensions/{id}` | Delete a Z-field value |

---

#### 9.3.10 Complete walkthrough — building a CUSTOMERS table from scratch

This step-by-step tutorial uses `curl` commands against the running application. Start the backend first (`./gradlew bootRun`), then follow each step in order. Each step builds on the previous one.

> **Note:** The `id` values in the responses below (e.g., `"id": 1`) are auto-generated by the database and may differ on your machine if other records already exist. Use the actual `id` values from your responses when referencing them in later steps.

**Step 1 — Create the Domains** (technical rules):

```bash
# Domain for customer names: text, max 100 characters
curl -s -X POST http://localhost:8080/api/v1/ddic/domains \
  -H "Content-Type: application/json" \
  -d '{
    "domainName": "CHAR_100",
    "dataType": "CHAR",
    "maxLength": 100,
    "description": "Character field up to 100 characters"
  }' | jq .
# Response: { "id": 1, "domainName": "CHAR_100", ... }

# Domain for country codes: text, max 3 characters
curl -s -X POST http://localhost:8080/api/v1/ddic/domains \
  -H "Content-Type: application/json" \
  -d '{
    "domainName": "CHAR_3",
    "dataType": "CHAR",
    "maxLength": 3,
    "description": "Character field up to 3 characters"
  }' | jq .
# Response: { "id": 2, "domainName": "CHAR_3", ... }

# Domain for monetary amounts: decimal, 13 digits, 2 decimal places
curl -s -X POST http://localhost:8080/api/v1/ddic/domains \
  -H "Content-Type: application/json" \
  -d '{
    "domainName": "DEC_13_2",
    "dataType": "DEC",
    "maxLength": 13,
    "decimalPlaces": 2,
    "description": "Decimal field for monetary amounts"
  }' | jq .
# Response: { "id": 3, "domainName": "DEC_13_2", ... }
```

**Step 2 — Create the Data Elements** (business meaning + labels):

```bash
# Data element for customer name (references domain id=1, CHAR_100)
curl -s -X POST http://localhost:8080/api/v1/ddic/data-elements \
  -H "Content-Type: application/json" \
  -d '{
    "elementName": "CUST_NAME",
    "domainId": 1,
    "shortLabel": "Name",
    "mediumLabel": "Cust Name",
    "longLabel": "Customer Name",
    "description": "Full legal name of the customer"
  }' | jq .
# Response: { "id": 1, "elementName": "CUST_NAME", "domainId": 1, "domainName": "CHAR_100", ... }

# Data element for country code (references domain id=2, CHAR_3)
curl -s -X POST http://localhost:8080/api/v1/ddic/data-elements \
  -H "Content-Type: application/json" \
  -d '{
    "elementName": "COUNTRY_CD",
    "domainId": 2,
    "shortLabel": "Ctry",
    "mediumLabel": "Country",
    "longLabel": "Country Code",
    "description": "ISO 3166-1 alpha-3 country code"
  }' | jq .
# Response: { "id": 2, "elementName": "COUNTRY_CD", "domainId": 2, "domainName": "CHAR_3", ... }

# Data element for credit limit (references domain id=3, DEC_13_2)
curl -s -X POST http://localhost:8080/api/v1/ddic/data-elements \
  -H "Content-Type: application/json" \
  -d '{
    "elementName": "CREDIT_LIMIT",
    "domainId": 3,
    "shortLabel": "Limit",
    "mediumLabel": "Credit Lmt",
    "longLabel": "Credit Limit Amount",
    "description": "Maximum credit limit in local currency"
  }' | jq .
# Response: { "id": 3, "elementName": "CREDIT_LIMIT", "domainId": 3, "domainName": "DEC_13_2", ... }
```

**Step 3 — Create the Table Definition**:

```bash
# Create the CUSTOMERS table at the CONCEPTUAL schema level
curl -s -X POST http://localhost:8080/api/v1/ddic/tables \
  -H "Content-Type: application/json" \
  -d '{
    "tableName": "CUSTOMERS",
    "schemaLevel": "CONCEPTUAL",
    "description": "Master data for all customers",
    "clientSpecific": true
  }' | jq .
# Response: { "id": 1, "tableName": "CUSTOMERS", "schemaLevel": "CONCEPTUAL", ... }
```

**Step 4 — Add Fields (columns) to the Table**:

```bash
# Field 1: CUST_NAME (position 1, not a key, not nullable, not an extension)
curl -s -X POST http://localhost:8080/api/v1/ddic/fields \
  -H "Content-Type: application/json" \
  -d '{
    "tableDefinitionId": 1,
    "fieldName": "CUST_NAME",
    "dataElementId": 1,
    "position": 1,
    "key": false,
    "nullable": false,
    "extension": false
  }' | jq .

# Field 2: COUNTRY (position 2, not a key, nullable, not an extension)
curl -s -X POST http://localhost:8080/api/v1/ddic/fields \
  -H "Content-Type: application/json" \
  -d '{
    "tableDefinitionId": 1,
    "fieldName": "COUNTRY",
    "dataElementId": 2,
    "position": 2,
    "key": false,
    "nullable": true,
    "extension": false
  }' | jq .

# Field 3: CREDIT_LIMIT (position 3, not a key, nullable, not an extension)
curl -s -X POST http://localhost:8080/api/v1/ddic/fields \
  -H "Content-Type: application/json" \
  -d '{
    "tableDefinitionId": 1,
    "fieldName": "CREDIT_LIMIT",
    "dataElementId": 3,
    "position": 3,
    "key": false,
    "nullable": true,
    "extension": false
  }' | jq .
```

**Step 5 — Verify what you built**:

```bash
# List all fields in the CUSTOMERS table (ordered by position)
curl -s "http://localhost:8080/api/v1/ddic/fields?tableDefinitionId=1" | jq .
# Returns an array of 3 fields: CUST_NAME, COUNTRY, CREDIT_LIMIT

# Filter only CONCEPTUAL tables
curl -s "http://localhost:8080/api/v1/ddic/tables?schemaLevel=CONCEPTUAL" | jq .
```

**Step 6 — Create a Search Help** (lookup dropdown):

```bash
# Search help for the CUSTOMERS table — enables a dropdown lookup
curl -s -X POST http://localhost:8080/api/v1/ddic/search-helps \
  -H "Content-Type: application/json" \
  -d '{
    "searchHelpName": "SH_CUSTOMER",
    "tableDefinitionId": 1,
    "description": "Search help for customer lookup"
  }' | jq .
```

At this point you have built a complete DDIC structure:

```
Domain CHAR_100           Domain CHAR_3           Domain DEC_13_2
    ↓                        ↓                       ↓
DataElement CUST_NAME    DataElement COUNTRY_CD   DataElement CREDIT_LIMIT
    ↓                        ↓                       ↓
Field CUST_NAME (pos 1)  Field COUNTRY (pos 2)   Field CREDIT_LIMIT (pos 3)
    └────────────────────────┴───────────────────────┘
                             ↓
              Table Definition: CUSTOMERS (CONCEPTUAL)
                             ↑
              Search Help: SH_CUSTOMER
```

---

#### 9.3.11 What are Z-fields and why do they exist?

**The problem:** Imagine the ERP Kernel is shipped to 50 different clients. A hospital client needs a `BLOOD_TYPE` field on their patient table. A retail client needs a `LOYALTY_TIER` field on their customer table. You cannot add these columns to the core database schema because:

1. The core team does not know every client's needs.
2. Modifying the core schema for one client would affect all other clients.
3. Each client's customisations would conflict with each other.

**The solution: Z-fields.** Any field whose name starts with `Z_` is a client-specific extension. The "Z" prefix is a convention borrowed from SAP — it means "this is custom, not part of the standard product."

There are **two ways** Z-fields work in this project:

**Way 1: Z-fields as Table Fields (schema-level):**

These are defined in the DDIC as Table Fields with `extension: true`. They describe **what custom fields exist** for a table — like adding a column definition. The service enforces that the field name starts with `Z_`:

```bash
# First, create a domain and data element for the Z-field
curl -s -X POST http://localhost:8080/api/v1/ddic/domains \
  -H "Content-Type: application/json" \
  -d '{
    "domainName": "CHAR_20",
    "dataType": "CHAR",
    "maxLength": 20,
    "description": "Character field up to 20 characters"
  }' | jq .
# Response: { "id": 4, ... }

curl -s -X POST http://localhost:8080/api/v1/ddic/data-elements \
  -H "Content-Type: application/json" \
  -d '{
    "elementName": "LOYALTY_TIER",
    "domainId": 4,
    "shortLabel": "Tier",
    "mediumLabel": "Loyalty",
    "longLabel": "Customer Loyalty Tier",
    "description": "Client-specific loyalty programme tier"
  }' | jq .
# Response: { "id": 4, ... }

# Now add the Z-field to the CUSTOMERS table
curl -s -X POST http://localhost:8080/api/v1/ddic/fields \
  -H "Content-Type: application/json" \
  -d '{
    "tableDefinitionId": 1,
    "fieldName": "Z_LOYALTY_TIER",
    "dataElementId": 4,
    "position": 4,
    "key": false,
    "nullable": true,
    "extension": true
  }' | jq .
```

> **Validation rule:** If `extension` is `true` but the field name does not start with `Z_`, the API returns a `400 Bad Request` with the message: _"Extension field name must start with 'Z_', got 'LOYALTY_TIER'"_.

You can query only the extension fields for a table:

```bash
curl -s "http://localhost:8080/api/v1/ddic/fields?tableDefinitionId=1&extensionsOnly=true" | jq .
# Returns only: Z_LOYALTY_TIER
```

**Way 2: Extension Field Values (data-level, EAV pattern):**

While Table Fields describe **what custom columns exist**, Extension Field Values store the **actual data** for those columns. This uses the **Entity-Attribute-Value (EAV)** pattern — instead of adding a physical column to a database table, you store each custom value as a separate row:

```
┌─────────────────────────────────────────────────────────────┐
│  Traditional approach (adding a column):                     │
│                                                              │
│  CUSTOMERS table:                                            │
│  | id | name   | country | credit_limit | Z_LOYALTY_TIER |   │
│  | 1  | Acme   | USA     | 50000.00     | GOLD           |   │
│  | 2  | GlobalCo| GBR    | 100000.00    | PLATINUM       |   │
│                                                              │
│  Problem: Requires ALTER TABLE — modifies the core schema!   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  EAV approach (what this project uses):                      │
│                                                              │
│  ddic_extension_field_value table:                           │
│  | table_name | record_id | field_name      | field_value |  │
│  | CUSTOMERS  | 1         | Z_LOYALTY_TIER  | GOLD        |  │
│  | CUSTOMERS  | 2         | Z_LOYALTY_TIER  | PLATINUM    |  │
│  | CUSTOMERS  | 1         | Z_REGION        | NORTHEAST   |  │
│                                                              │
│  Advantage: No schema changes needed! Just insert rows.      │
└─────────────────────────────────────────────────────────────┘
```

Here is how to store and retrieve Z-field values:

```bash
# Store a Z-field value: customer #1 has loyalty tier "GOLD"
curl -s -X POST http://localhost:8080/api/v1/ddic/extensions \
  -H "Content-Type: application/json" \
  -d '{
    "tableName": "CUSTOMERS",
    "recordId": 1,
    "fieldName": "Z_LOYALTY_TIER",
    "fieldValue": "GOLD"
  }' | jq .

# Store another Z-field value: customer #1 also has region "NORTHEAST"
curl -s -X POST http://localhost:8080/api/v1/ddic/extensions \
  -H "Content-Type: application/json" \
  -d '{
    "tableName": "CUSTOMERS",
    "recordId": 1,
    "fieldName": "Z_REGION",
    "fieldValue": "NORTHEAST"
  }' | jq .

# Retrieve all Z-field values for customer #1
curl -s "http://localhost:8080/api/v1/ddic/extensions?tableName=CUSTOMERS&recordId=1" | jq .
# Returns: [ { "fieldName": "Z_LOYALTY_TIER", "fieldValue": "GOLD" },
#             { "fieldName": "Z_REGION", "fieldValue": "NORTHEAST" } ]

# Update a Z-field value: change loyalty tier to "PLATINUM"
# The save endpoint is an UPSERT — if the value exists, it updates; otherwise, it creates
curl -s -X POST http://localhost:8080/api/v1/ddic/extensions \
  -H "Content-Type: application/json" \
  -d '{
    "tableName": "CUSTOMERS",
    "recordId": 1,
    "fieldName": "Z_LOYALTY_TIER",
    "fieldValue": "PLATINUM"
  }' | jq .
```

> **Validation rule:** Extension field names must start with `Z_`. If you try to create a value with field name `LOYALTY_TIER` (without the `Z_` prefix), the API returns `400 Bad Request`.

---

#### 9.3.12 How to add a new DDIC feature as a developer

If you need to add a new DDIC component (e.g., a "Structure" type), follow this pattern:

1. **Entity** — Create the JPA entity in `ddic/entity/` extending `BaseEntity`
2. **Repository** — Create a Spring Data JPA repository in `ddic/repository/`
3. **DTO** — Create a `Create*Request` record and a `*Dto` record in `ddic/dto/`
4. **Mapper** — Create a mapper class in `ddic/mapper/` with `toEntity()`, `toDto()`, and `updateEntity()` methods
5. **Service** — Create a service in `ddic/service/` with create/findById/findAll/update/delete methods
6. **Controller** — Create a REST controller in `ddic/controller/` mapping to `/api/v1/ddic/your-resource`
7. **Migration** — Create a new Flyway migration (`V8__your_table.sql`) in `src/main/resources/db/migration/`
8. **Tests** — Write unit tests for the service and `@WebMvcTest` tests for the controller with 100% coverage

> **Tip:** Copy an existing DDIC component (like Domain) as a starting point. The pattern is identical for every component.

---

#### 9.3.13 DDIC quick reference

| What you want to do | API call | Notes |
|---|---|---|
| Define a new data type rule | `POST /api/v1/ddic/domains` | Always create domains first |
| Give a field business meaning | `POST /api/v1/ddic/data-elements` | Must reference an existing domain ID |
| Register a logical table | `POST /api/v1/ddic/tables` | Choose schema level: EXTERNAL, CONCEPTUAL, or INTERNAL |
| Add a column to a table | `POST /api/v1/ddic/fields` | Must reference a table ID and data element ID |
| Add a client-custom column | `POST /api/v1/ddic/fields` with `extension: true` | Field name must start with `Z_` |
| Store a custom value for a record | `POST /api/v1/ddic/extensions` | Field name must start with `Z_`; works as upsert |
| Get all custom values for a record | `GET /api/v1/ddic/extensions?tableName=X&recordId=1` | Returns all Z-field values |
| Create a dropdown lookup | `POST /api/v1/ddic/search-helps` | Must reference a table definition ID |
| List only CONCEPTUAL tables | `GET /api/v1/ddic/tables?schemaLevel=CONCEPTUAL` | Filter by schema level |
| List only Z-fields in a table | `GET /api/v1/ddic/fields?tableDefinitionId=1&extensionsOnly=true` | Shows only extension fields |

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
