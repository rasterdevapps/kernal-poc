# ADR-0002: ANSI/SPARC Three-Schema Database Architecture

| Field       | Value                                               |
|-------------|-----------------------------------------------------|
| **Status**  | Accepted                                            |
| **Date**    | 2025-02-01                                          |
| **Phase**   | Phase 2 — Database Architecture & Abstraction Layer |
| **Milestone** | 2.1 — ANSI/SPARC Three-Schema Architecture        |

---

## Context

Enterprise ERP systems require a database abstraction layer that decouples application
logic from physical storage. SAP's Data Dictionary (DDIC / SE11) has proven this
approach at scale for decades — allowing schema evolution, client customisation, and
multi-database portability without breaking application code.

The ERP Kernel must support:

- **Multiple consumers** viewing the same data differently (role-specific projections).
- **Schema evolution** without cascading changes through application layers.
- **Client-specific extensions** ("Z" fields) on standard tables.
- **Metadata-driven services** for table definitions, data elements, and domains.
- **Dual database profiles**: PostgreSQL 18 (production) and H2DB (local/test).

The ANSI/SPARC three-schema architecture (1975) provides a well-established theoretical
framework that directly maps to these requirements.

## Decision

We implement the **ANSI/SPARC three-schema architecture** as the database abstraction
layer, modelled after SAP's DDIC. The architecture is represented by the `SchemaLevel`
enum in `com.erp.kernel.ddic.model` with three values:

| Schema Level   | Purpose                              | ERP Kernel Mapping                    |
|----------------|--------------------------------------|---------------------------------------|
| **EXTERNAL**   | User/application view of data        | DTOs, API projections, search helps   |
| **CONCEPTUAL** | Logical structure of entire database | Entity model, table/field definitions |
| **INTERNAL**   | Physical storage representation      | Flyway migrations, indexes, partitions|

### Implementation components

| Component                  | Package                    | Responsibility                           |
|----------------------------|----------------------------|------------------------------------------|
| `SchemaLevel` enum         | `ddic.model`               | Defines the three abstraction levels     |
| `TableDefinition` entity   | `ddic.model`               | Metadata for logical table structure     |
| `TableField` entity        | `ddic.model`               | Column-level metadata with type mapping  |
| `DataElement` entity       | `ddic.model`               | Reusable semantic field descriptions     |
| `Domain` entity            | `ddic.model`               | Value ranges, formats, and constraints   |
| `SearchHelp` entity        | `ddic.model`               | Input assistance configuration           |
| `ExtensionFieldService`    | `ddic.service`             | "Z" field extension management           |
| `TableDefinitionService`   | `ddic.service`             | CRUD for table metadata                  |
| `V2__ddic_schema.sql`      | `db/migration`             | Flyway migration for DDIC tables         |

### Mapping to SAP DDIC concepts

| SAP DDIC Concept | ERP Kernel Equivalent | Schema Level |
|------------------|-----------------------|--------------|
| Table (SE11)     | `TableDefinition`     | CONCEPTUAL   |
| Field            | `TableField`          | CONCEPTUAL   |
| Data Element     | `DataElement`         | CONCEPTUAL   |
| Domain           | `Domain`              | CONCEPTUAL   |
| Search Help      | `SearchHelp`          | EXTERNAL     |
| DB Index         | Flyway migration DDL  | INTERNAL     |

## Consequences

### Positive

- **Decoupled layers**: Application code depends on the conceptual schema only;
  physical storage changes (indexes, partitioning) do not propagate upward.
- **Client extensibility**: "Z" fields attach to the external schema without modifying
  core conceptual or internal schemas, enabling safe client customisation.
- **Metadata-driven**: Table definitions, domains, and data elements are queryable at
  runtime, enabling dynamic UI generation and validation.
- **Multi-database portability**: The conceptual layer abstracts database-specific SQL
  behind JPA and Flyway, supporting PostgreSQL 18 and H2DB transparently.
- **Standards alignment**: ANSI/SPARC is an ISO-recognised architecture, aligning with
  the project's ISO/IEC/IEEE 12207 compliance goals.

### Negative

- **Added complexity**: Three distinct schema layers require developers to understand
  which layer they are modifying. Mitigated by clear package structure and
  documentation.
- **Performance overhead**: Metadata lookups add latency compared to direct ORM access.
  Mitigated by Caffeine caching (milestone 2.5) for frequently accessed definitions.
- **Learning curve**: Developers unfamiliar with SAP DDIC concepts need onboarding.
  Mitigated by this ADR and the framework documentation (Phase 9.3).

---

*Relates to: Phase 2 milestones 2.1–2.6. Builds on ADR-0001 (technology stack).*
