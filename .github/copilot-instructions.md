# Copilot Instructions — Java SonarLint Coding Standards & Unit Testing

> **Project:** ERP Kernel PoC
> **Tech Stack:** Java 25 · Spring Boot · PostgreSQL 18 · H2DB (local) · Gradle/Maven · GitHub Actions

---

## 1. General Java Coding Standards (SonarLint Aligned)

### 1.1 Naming Conventions

- Use **PascalCase** for class and interface names (e.g., `UserService`, `OrderRepository`).
- Use **camelCase** for method names, variable names, and parameter names (e.g., `getUserById`, `orderCount`).
- Use **UPPER_SNAKE_CASE** for constants (e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`).
- Use **all lowercase** for package names, no underscores or mixed case (e.g., `com.erp.kernel.service`).
- Boolean variables and methods should read as predicates (e.g., `isActive`, `hasPermission`, `canExecute`).
- Avoid single-character variable names except in short lambdas or loop indices (`i`, `j`, `k`).
- Avoid generic names like `data`, `info`, `temp`, `obj`; prefer descriptive, domain-specific names.

### 1.2 Code Structure & Formatting

- Limit line length to **120 characters**.
- Use **4 spaces** for indentation; never use tabs.
- Place opening braces `{` on the **same line** as the declaration.
- Always use braces `{}` for `if`, `else`, `for`, `while`, and `do-while` blocks, even for single-line bodies.
- Limit methods to a maximum of **30 lines** of logic (excluding blank lines, comments, and braces).
- Limit classes to a maximum of **500 lines** (excluding imports and comments).
- Limit method parameters to a maximum of **7**; prefer a parameter object for more.
- Declare one variable per line; do not combine declarations.

### 1.3 Imports

- Never use wildcard imports (e.g., `import java.util.*`). Always use explicit imports.
- Remove unused imports.
- Order imports: `java.*`, `javax.*`, blank line, third-party libraries, blank line, project packages.

### 1.4 Null Safety & Optionals

- Never return `null` from methods; use `Optional<T>` for values that may be absent.
- Use `Objects.requireNonNull()` for mandatory parameters in public methods.
- Prefer `Optional.ofNullable()` over null checks where appropriate.
- Avoid using `Optional` as method parameters or field types; use it only for return types.

### 1.5 Exception Handling

- Never catch generic `Exception` or `Throwable`; catch specific exception types.
- Never swallow exceptions with empty catch blocks; at minimum, log the exception.
- Use custom exception classes for domain-specific errors (e.g., `EntityNotFoundException`, `ValidationException`).
- Include meaningful messages in exceptions that describe the context and cause.
- Prefer unchecked exceptions (`RuntimeException` subclasses) for unrecoverable errors.
- Use checked exceptions only when the caller can reasonably handle the error.
- Always close resources using try-with-resources.

### 1.6 Immutability & Data Integrity

- Prefer `final` for local variables, method parameters, and fields that do not change.
- Use Java `record` types for immutable data carriers (DTOs, value objects).
- Return unmodifiable collections from methods (`List.of()`, `Collections.unmodifiableList()`).
- Avoid mutable static fields; use constants or dependency injection instead.

### 1.7 String Handling

- Use `String.isEmpty()` or `String.isBlank()` instead of comparing with `""` or checking length.
- Use `StringBuilder` for string concatenation inside loops.
- Use text blocks (`"""`) for multi-line strings.
- Prefer `String.formatted()` or `String.format()` over string concatenation for complex strings.

### 1.8 Collections & Streams

- Prefer `List.of()`, `Set.of()`, `Map.of()` for creating immutable collections.
- Use the diamond operator `<>` for generic type inference.
- Prefer `Stream` API over imperative loops for data transformations.
- Avoid side effects in stream operations; streams should be functional.
- Prefer `forEach` only for terminal operations with side effects; use `map`, `filter`, `collect` for transformations.

### 1.9 Concurrency

- Never use `Thread.sleep()` in production code for synchronisation; use proper concurrency utilities.
- Prefer `CompletableFuture` for asynchronous programming.
- Use `java.util.concurrent` utilities over manual thread management.
- Ensure thread-safe access to shared mutable state using `synchronized`, `Lock`, or atomic classes.

### 1.10 Code Smells to Avoid

- **Dead code:** Remove unreachable or unused code (methods, variables, imports, parameters).
- **Code duplication:** Extract repeated logic into shared methods or utility classes.
- **God classes:** Split classes with too many responsibilities into smaller, focused classes.
- **Long methods:** Refactor methods longer than 30 lines into smaller, well-named helper methods.
- **Deep nesting:** Limit nesting depth to 3 levels; use early returns or extract methods.
- **Magic numbers/strings:** Replace literals with named constants.
- **Cognitive complexity:** Keep methods below a cognitive complexity of 15 (SonarLint rule `java:S3776`).
- **Commented-out code:** Remove commented-out code; use version control for history.

---

## 2. SonarLint Critical & Major Rules

Always comply with the following SonarLint rule categories:

### 2.1 Bugs

- Do not use `==` to compare strings; use `.equals()` (`java:S4973`).
- Do not use `==` to compare boxed types; use `.equals()` (`java:S5838`).
- Ensure `equals()` and `hashCode()` are overridden together (`java:S1206`).
- Do not compare with `null` using `equals()`; use `==` for null checks (`java:S4165`).
- Close resources in a `finally` block or use try-with-resources (`java:S2095`).

### 2.2 Vulnerabilities

- Do not hard-code credentials, passwords, or secrets (`java:S6437`).
- Do not log sensitive information (passwords, tokens, personal data) (`java:S5145`).
- Validate and sanitise all external inputs before use (`java:S5144`).
- Use parameterised queries; never concatenate user input into SQL statements (`java:S3649`).
- Do not disable certificate validation (`java:S4830`).

### 2.3 Code Smells

- Remove unused private methods (`java:S1144`).
- Remove unused local variables (`java:S1481`).
- Remove unused method parameters (`java:S1172`).
- Replace nested ternary operators with `if/else` blocks (`java:S3358`).
- Collapse multiple `if` statements into a single condition when possible (`java:S1066`).
- Use `var` for local variable type inference where the type is obvious from the right-hand side.
- Prefer enhanced `switch` expressions over traditional `switch` statements (`java:S1479`).

---

## 3. Spring Boot Specific Standards

### 3.1 Architecture & Layering

- Follow a **layered architecture**: Controller → Service → Repository.
- Controllers handle HTTP concerns only; no business logic in controllers.
- Services contain business logic and transaction management.
- Repositories handle data access only.
- Use DTOs for API request/response; never expose entity objects directly through APIs.

### 3.2 Dependency Injection

- Use **constructor injection** exclusively; avoid field injection (`@Autowired` on fields).
- Mark injected dependencies as `private final`.
- Use `@RequiredArgsConstructor` (Lombok) or explicit constructors for injection.

### 3.3 Configuration

- Use `@ConfigurationProperties` for type-safe configuration binding.
- Use Spring profiles (`application-dev.yml`, `application-prod.yml`) for environment-specific configuration.
- Never hard-code environment-specific values; externalise all configuration.

### 3.4 REST API Design

- Use proper HTTP methods: `GET` (read), `POST` (create), `PUT` (full update), `PATCH` (partial update), `DELETE` (remove).
- Return appropriate HTTP status codes (200, 201, 204, 400, 401, 403, 404, 409, 500).
- Use `@Valid` for request body validation with Jakarta Bean Validation annotations.
- Version APIs in the URL path (e.g., `/api/v1/users`).

### 3.5 Logging

- Use SLF4J with parameterised logging (`log.info("User {} created", userId)`); never concatenate strings in log statements.
- Use appropriate log levels: `ERROR` for failures, `WARN` for potential issues, `INFO` for significant events, `DEBUG` for development diagnostics.
- Never log sensitive data (passwords, tokens, personal information).

---

## 4. Unit Testing Standards — 100% Code Coverage

### 4.1 Coverage Requirements

- **All new code must achieve 100% line coverage and 100% branch coverage.**
- Every public method must have at least one corresponding test method.
- Every conditional branch (`if/else`, `switch`, ternary) must be tested for all possible paths.
- Every exception path must be tested (verify the exception is thrown with the correct type and message).
- All edge cases and boundary conditions must have dedicated tests.
- Code coverage is measured using **JaCoCo** and enforced in the CI/CD pipeline.

### 4.2 Testing Framework & Libraries

- Use **JUnit 5** (`org.junit.jupiter`) as the testing framework.
- Use **Mockito** for mocking dependencies.
- Use **AssertJ** for fluent, readable assertions (preferred over JUnit assertions).
- Use **Spring Boot Test** (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`) for integration tests.
- Use **H2DB** as the in-memory database for repository-level tests.

### 4.3 Test Naming Conventions

- Name test classes as `<ClassUnderTest>Test` (e.g., `UserServiceTest`).
- Name test methods using the pattern: `should<ExpectedBehavior>_when<Condition>` (e.g., `shouldReturnUser_whenUserExists`, `shouldThrowException_whenUserNotFound`).
- Each test method must test exactly one behaviour; do not combine multiple assertions for unrelated behaviours.

### 4.4 Test Structure

- Follow the **Arrange-Act-Assert (AAA)** pattern in every test:
  ```java
  @Test
  void shouldReturnUser_whenUserExists() {
      // Arrange
      var userId = 1L;
      var expectedUser = new User(userId, "John");
      when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

      // Act
      var result = userService.getUserById(userId);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.name()).isEqualTo("John");
  }
  ```
- Use `@BeforeEach` for shared test setup; avoid duplicating setup code across tests.
- Use `@ParameterizedTest` with `@ValueSource`, `@CsvSource`, `@MethodSource`, or `@EnumSource` for testing multiple inputs with the same logic.
- Keep tests independent; tests must not depend on execution order or shared mutable state.

### 4.5 Mocking Guidelines

- Mock only external dependencies (repositories, external services, clients); do not mock the class under test.
- Use `@Mock` for dependencies and `@InjectMocks` for the class under test.
- Use `@ExtendWith(MockitoExtension.class)` at the class level.
- Verify interactions with mocks using `verify()` when the interaction is part of the expected behaviour.
- Use `verifyNoMoreInteractions()` to ensure no unexpected calls are made when relevant.
- Prefer `when(...).thenReturn(...)` over `doReturn(...).when(...)` unless mocking void methods.

### 4.6 What to Test

| Layer | Test Type | What to Verify |
|-------|-----------|----------------|
| **Service** | Unit test | Business logic, validations, exception handling, all branches |
| **Controller** | `@WebMvcTest` | Request mapping, input validation, HTTP status codes, response structure |
| **Repository** | `@DataJpaTest` | Custom queries, query correctness, entity mappings |
| **DTOs / Records** | Unit test | Equality, serialisation/deserialisation, builder patterns |
| **Utility classes** | Unit test | All methods, edge cases, null inputs, boundary values |
| **Exception classes** | Unit test | Exception message, cause, custom fields |
| **Mappers** | Unit test | All mapping paths, null handling, nested object mapping |
| **Configuration** | `@SpringBootTest` | Bean creation, property binding, profile activation |

### 4.7 What Not to Do in Tests

- Do not use `@SpringBootTest` for unit tests; reserve it for integration tests only.
- Do not connect to external services or databases in unit tests; mock all external dependencies.
- Do not ignore or disable tests with `@Disabled` without a linked issue or explanation.
- Do not use `Thread.sleep()` in tests; use Awaitility or `CompletableFuture` for async testing.
- Do not hard-code file paths or environment-specific values in tests.
- Do not write tests that pass regardless of the implementation (always verify meaningful behaviour).

### 4.8 Coverage Enforcement

- Configure **JaCoCo** in the build to fail if coverage drops below 100% for new code:
  ```groovy
  // build.gradle example
  jacocoTestCoverageVerification {
      violationRules {
          rule {
              limit {
                  counter = 'LINE'
                  value = 'COVEREDRATIO'
                  minimum = 1.0
              }
              limit {
                  counter = 'BRANCH'
                  value = 'COVEREDRATIO'
                  minimum = 1.0
              }
          }
      }
  }
  ```
- Run `./gradlew test jacocoTestReport jacocoTestCoverageVerification` (or equivalent Maven commands) to validate coverage.
- Review the JaCoCo HTML report at `build/reports/jacoco/test/html/index.html` to identify uncovered lines.

---

## 5. Documentation Standards

- Every public class must have a Javadoc comment describing its purpose and responsibilities.
- Every public method must have a Javadoc comment describing its behaviour, parameters (`@param`), return value (`@return`), and exceptions (`@throws`).
- Use inline comments sparingly; code should be self-documenting through clear naming.
- Keep README and MILESTONES documentation up to date when making architectural changes.

---

## 6. Version Control Standards

- Write clear, concise commit messages in the imperative mood (e.g., "Add user service", "Fix null pointer in order processing").
- Each commit should represent a single logical change.
- Use feature branches with the naming pattern: `feature/<short-description>`, `bugfix/<short-description>`, `hotfix/<short-description>`.
- All code must pass linting, build, and tests before merging.
