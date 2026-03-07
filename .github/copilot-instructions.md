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

## 6. Milestone Tracking

- **Every feature change must update `MILESTONES.md`** to reflect the current status of the affected milestone(s).
- Use the following status indicators in the **Status** column of each milestone table:

| Icon | Meaning |
|------|---------|
| ✅ | **Completed** — Milestone is fully implemented, tested, and merged. |
| 🔄 | **In Progress** — Milestone is actively being worked on. |
| ⬜ | **Not Started** — Milestone has not been started yet. |

- When starting work on a milestone, set its status to 🔄 **In Progress**.
- When the milestone is fully implemented with all tests passing and code merged, set its status to ✅ **Completed**.
- Update the status in the same PR that delivers the feature change; do not defer milestone updates to a separate PR.
- If a feature change partially completes a milestone, keep the status as 🔄 **In Progress** and add a note in the description.

---

## 7. Version Control Standards

- Write clear, concise commit messages in the imperative mood (e.g., "Add user service", "Fix null pointer in order processing").
- Each commit should represent a single logical change.
- Use feature branches with the naming pattern: `feature/<short-description>`, `bugfix/<short-description>`, `hotfix/<short-description>`.
- All code must pass linting, build, and tests before merging.

---

## 8. ISO/IEC/IEEE 12207 — Software Lifecycle Processes

> **Reference:** [ISO/IEC/IEEE 12207:2017](https://en.wikipedia.org/wiki/ISO/IEC_12207) — Systems and software engineering — Software life cycle processes.

All feature development must comply with the following ISO/IEC/IEEE 12207 process areas. Each process area maps to activities that must be evidenced before a feature is considered complete.

### 8.1 Technical Processes

| Process | Required Activities |
|---------|-------------------|
| **Stakeholder Needs & Requirements** | Capture requirements in the GitHub issue before development begins. Acceptance criteria must be defined and traceable. |
| **System/Software Requirements Analysis** | Translate stakeholder needs into technical requirements documented in the issue or linked design document. |
| **Architecture & Design** | Document design decisions in code comments, ADRs (Architecture Decision Records), or the PR description. Ensure alignment with the layered architecture (Controller → Service → Repository). |
| **Implementation** | Follow all coding standards defined in Sections 1–3 of this document. All code must pass static analysis (SonarLint) and compile without warnings. |
| **Integration** | Ensure new code integrates with existing modules without breaking changes. Run the full test suite before merging. |
| **Verification** | Achieve 100% line and branch coverage (Section 4). All tests must pass. Perform peer code review on every PR. |
| **Validation** | Confirm the implementation satisfies the original acceptance criteria. Demonstrate functionality in the PR description or linked artefact. |
| **Transition** | Update deployment configuration, migration scripts, and environment-specific settings as needed. Ensure backward compatibility. |
| **Maintenance** | Document known limitations or technical debt in GitHub issues. Update `MILESTONES.md` and `README.md` as required. |

### 8.2 Agreement & Organisational Processes

| Process | Required Activities |
|---------|-------------------|
| **Configuration Management** | Use Git for version control. Follow branch and commit conventions (Section 7). Tag releases appropriately. |
| **Quality Assurance** | Enforce coding standards via automated linting, static analysis, and CI/CD pipeline checks. Every PR must pass all quality gates before merge. |
| **Review & Audit** | Every PR must have at least one peer review. Reviewers must verify compliance with coding standards, test coverage, and design principles. |
| **Decision Management** | Record significant technical decisions as ADRs or in PR descriptions. Reference the relevant milestone and issue number. |
| **Risk Management** | Identify and document risks (security, performance, compatibility) in the PR description. Mitigate risks before merging or log them as follow-up issues. |

### 8.3 Traceability Requirements

- Every commit must reference a GitHub issue number (e.g., `Fix #42: Add user validation`).
- Every PR must link to the milestone it advances in `MILESTONES.md`.
- Every requirement must be traceable from issue → design → implementation → test → deployment.
- Maintain a clear audit trail: issue creation → branch → commits → PR → review → merge → milestone update.

---

## 9. CMMI Level 5 — Continuous Process Improvement

> **Reference:** CMMI Level 5 (Optimizing) requires quantitative process management, causal analysis, and continuous improvement across all process areas.

### 9.1 Quantitative Process Management

- **Measure build and test metrics:** Track build duration, test pass rate, test coverage percentage, and defect density per feature.
- **Monitor CI/CD pipeline health:** Record pipeline success/failure rates and mean time to resolution for pipeline failures.
- **Track code quality trends:** Use SonarLint/SonarQube dashboards to monitor code smell count, bug count, vulnerability count, and technical debt over time.
- **Measure review turnaround:** Track the time from PR creation to first review and from first review to merge.

### 9.2 Causal Analysis & Resolution (CAR)

- **Root cause analysis for defects:** When a bug is found in production or during testing, perform root cause analysis and document findings in the issue.
- **Preventive actions:** After identifying a root cause, implement preventive measures (additional tests, linting rules, process changes) and document them.
- **Post-incident reviews:** Conduct post-incident reviews for critical failures. Document the timeline, root cause, impact, resolution, and preventive actions.
- **Defect pattern tracking:** Categorise defects by type (logic error, null pointer, concurrency, security) and identify recurring patterns for targeted improvement.

### 9.3 Organisational Performance Management (OPM)

- **Process improvement proposals:** Any team member may propose process improvements via GitHub issues labelled `process-improvement`.
- **Retrospective actions:** Regularly review development processes and update this document, `MILESTONES.md`, and CI/CD workflows based on lessons learned.
- **Standards evolution:** Update coding standards, testing practices, and review checklists based on defect analysis and industry best practices.
- **Knowledge sharing:** Document reusable patterns, common pitfalls, and architectural decisions in the project wiki or ADRs.

### 9.4 Process & Product Quality Assurance (PPQA)

- **Compliance audits:** Periodically audit PRs and code against the standards defined in this document. Document audit results and corrective actions.
- **Quality gates:** Every feature must pass the following quality gates before merge:
  1. All automated tests pass (unit, integration).
  2. 100% line and branch coverage for new code (JaCoCo).
  3. Zero critical or major SonarLint findings.
  4. At least one peer review approval.
  5. PR description includes acceptance criteria verification.
  6. `MILESTONES.md` updated to reflect current status.
- **Continuous improvement of quality gates:** Review and enhance quality gate criteria quarterly based on defect analysis and process metrics.

---

## 10. Feature Completion Checklist

> Every feature must satisfy all items in this checklist before the PR is merged. This checklist combines ISO/IEC/IEEE 12207 and CMMI Level 5 requirements into a single actionable process.

### 10.1 Pre-Development

- [ ] GitHub issue created with clear description and acceptance criteria.
- [ ] Issue linked to the relevant milestone in `MILESTONES.md`.
- [ ] Design approach documented (in issue, ADR, or PR description).
- [ ] Risks identified and documented (security, performance, compatibility).
- [ ] Milestone status set to 🔄 **In Progress** in `MILESTONES.md`.

### 10.2 During Development

- [ ] Code follows all standards in Sections 1–3 (naming, structure, Spring Boot conventions).
- [ ] All new code has Javadoc comments on public classes and methods (Section 5).
- [ ] Unit tests written following Section 4 standards (AAA pattern, descriptive names).
- [ ] 100% line and branch coverage achieved for new code.
- [ ] No critical or major SonarLint findings introduced.
- [ ] Database migration scripts added if schema changes are required.
- [ ] Configuration externalised (no hard-coded environment-specific values).

### 10.3 Pre-Merge Review

- [ ] All automated tests pass (`./gradlew test jacocoTestReport jacocoTestCoverageVerification`).
- [ ] CI/CD pipeline passes all stages (build, test, static analysis).
- [ ] PR description includes: summary of changes, acceptance criteria verification, and risk assessment.
- [ ] At least one peer review completed and approved.
- [ ] Reviewer verified compliance with coding standards and test coverage.
- [ ] Commit messages follow conventions (imperative mood, issue reference).
- [ ] No unnecessary files committed (build artefacts, IDE settings, temporary files).

### 10.4 Post-Merge

- [ ] `MILESTONES.md` updated — milestone status set to ✅ **Completed** (or remains 🔄 if partially complete with a note).
- [ ] Related GitHub issues closed with a reference to the merged PR.
- [ ] Deployment configuration updated if required.
- [ ] `README.md` updated if the change affects project setup, architecture, or usage.
- [ ] Lessons learned documented (if applicable) for continuous improvement (Section 9).
- [ ] Metrics recorded: test coverage, build duration, defect count (Section 9.1).
