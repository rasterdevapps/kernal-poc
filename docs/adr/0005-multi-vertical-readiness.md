# ADR-0005: Multi-Vertical Deployment Readiness

| Field       | Value                                              |
|-------------|----------------------------------------------------|
| **Status**  | Accepted                                           |
| **Date**    | 2025-03-15                                         |
| **Phase**   | Phase 9 — ERP Framework Finalization & Extensibility |
| **Milestone** | 9.4 — Multi-Vertical Readiness                    |

---

## Context

The ERP Kernel is designed as a **horizontal platform** that serves multiple industry
verticals. Each vertical has unique domain requirements:

| Vertical        | Example Requirements                                    |
|-----------------|---------------------------------------------------------|
| Healthcare      | Patient records, HL7/FHIR interoperability, One Health  |
| Manufacturing   | Bill of materials, production scheduling, quality control|
| Retail          | POS integration, inventory management, supply chain     |
| Finance         | General ledger, regulatory reporting, audit trails      |
| Education       | Student enrolment, curriculum management, grading       |

The framework must ensure that:

- Vertical-specific modules can be developed and deployed independently.
- The core framework validates that all required capabilities are available before
  a vertical module is activated.
- Multiple verticals can coexist in the same deployment without interference.
- New verticals can be added without modifying the core framework.

## Decision

We implement a **vertical registration and validation system** in
`com.erp.kernel.vertical` that uses a type-safe enum, immutable descriptors, and
a thread-safe registry.

### Vertical type enumeration

The `VerticalType` enum defines supported industry verticals with human-readable
display names:

```java
public enum VerticalType {
    HEALTHCARE("Healthcare & One Health"),
    MANUFACTURING("Manufacturing & Production"),
    RETAIL("Retail & Distribution"),
    FINANCE("Financial Services"),
    EDUCATION("Education & Academic");
}
```

### Vertical descriptor validation

`VerticalDescriptor` is an immutable Java record that captures vertical module
metadata and enforces validation at construction time:

| Field                  | Type               | Validation                    |
|------------------------|--------------------|-------------------------------|
| `id`                   | `String`           | Non-null, non-blank           |
| `name`                 | `String`           | Non-null, non-blank           |
| `verticalType`         | `VerticalType`     | Non-null                      |
| `version`              | `String`           | Non-null, non-blank           |
| `description`          | `String`           | Non-null                      |
| `requiredCapabilities` | `Set<String>`      | Non-null (may be empty)       |

The `requiredCapabilities` field lists framework capabilities (e.g., `"encryption"`,
`"number-ranges"`, `"caching"`) that must be available for the vertical to operate
correctly. The registry validates these capabilities during registration.

### Core components

| Component            | Type        | Responsibility                               |
|----------------------|-------------|----------------------------------------------|
| `VerticalType`       | Enum        | Industry vertical enumeration                |
| `VerticalModule`     | Interface   | Contract for vertical-specific modules       |
| `VerticalDescriptor` | Record      | Immutable metadata with constructor validation|
| `VerticalRegistry`   | Component   | Thread-safe registration and lookup          |
| `VerticalController` | Controller  | REST API for vertical module operations      |

### Registry operations

| Operation                   | Description                                       |
|-----------------------------|---------------------------------------------------|
| `register(descriptor)`      | Validates and registers a vertical module         |
| `findById(id)`              | Lookup by unique module identifier                |
| `findByVerticalType(type)`  | Lookup all modules for a given industry vertical  |
| `getAll()`                  | List all registered vertical modules              |

The registry prevents duplicate module registrations (same ID) and uses thread-safe
collections for concurrent access.

### Integration with plugin architecture (ADR-0003)

Vertical modules are a specialisation of the plugin architecture:

- Each vertical is packaged as one or more **plugins** (ADR-0003).
- Vertical plugins register with both the `PluginRegistry` and `VerticalRegistry`.
- The `VerticalDescriptor.requiredCapabilities` are checked against the available
  extension points in the `ExtensionRegistry`.

## Consequences

### Positive

- **Type safety**: The `VerticalType` enum prevents invalid vertical identifiers and
  enables compile-time checking in switch expressions.
- **Capability validation**: Required capabilities are validated at registration,
  failing fast if the deployment lacks necessary framework features.
- **Coexistence**: Multiple verticals can be registered and active simultaneously,
  enabling multi-industry deployments.
- **Discoverability**: The REST API allows administrators to inspect which verticals
  are deployed and their status.
- **Extensibility**: Adding a new vertical requires only adding an enum value and
  implementing the `VerticalModule` interface.

### Negative

- **Enum rigidity**: Adding a new `VerticalType` requires a code change and
  redeployment. Mitigated by the expectation that industry verticals change
  infrequently.
- **Capability naming**: Required capabilities are identified by string names, which
  are not compile-time checked. Mitigated by centralised capability name constants.
- **No runtime isolation**: Verticals share the same JVM and Spring context. True
  multi-tenant isolation (separate databases, class loaders) is deferred to a future
  milestone.

---

*Relates to: Phase 9 milestone 9.4. Builds on ADR-0003 (plugin architecture) and
ADR-0001 (Java records, sealed classes).*
