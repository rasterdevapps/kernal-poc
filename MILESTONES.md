# ERP Kernel — Development Milestones

> **Tech Stack:** Java 25 · Spring Boot · PostgreSQL 18 · H2DB (local) · DB Versioning · CI/CD with GitHub Actions

> **Status Legend:** ✅ Completed · 🔄 In Progress · ⬜ Not Started

---

## Phase 1 — Foundation & Infrastructure Setup

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 1.1 | **Project Scaffolding** | Initialize Spring Boot project with Java 25, configure Gradle/Maven build, establish package structure and consistent naming conventions. | ⬜ Not Started |
| 1.2 | **Database Provisioning** | Set up PostgreSQL 18 for all non-local environments and H2DB for local development with profile-based switching. | ⬜ Not Started |
| 1.3 | **DB Versioning** | Integrate Flyway or Liquibase for database schema versioning with migration scripts and rollback support. | ⬜ Not Started |
| 1.4 | **CI/CD Pipeline** | Configure GitHub Actions workflows for build, test, static analysis, and deployment stages. | ⬜ Not Started |
| 1.5 | **Development Environment Standards** | Define coding standards, branch strategy, PR templates, commit conventions, and ISO/IEC/IEEE 12207 & CMMI Level 5 process documentation to ensure consistency, traceability, and continuous improvement across contributors. | ✅ Completed |

---

## Phase 2 — Database Architecture & Abstraction Layer

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 2.1 | **ANSI/SPARC Three-Schema Architecture** | Implement the three-schema model (External, Conceptual, Internal) for database abstraction, inspired by ABAP's Data Dictionary (DDIC). | ⬜ Not Started |
| 2.2 | **Data Dictionary (DDIC) Service** | Build a metadata-driven data dictionary service that manages table definitions, domains, data elements, and search helps similar to SAP SE11. | ⬜ Not Started |
| 2.3 | **Password-Protected Database Access** | Enforce credential-based database access controls so that clients cannot modify schemas or data directly; all access goes through the application layer. | ⬜ Not Started |
| 2.4 | **Client Customisation via "Z" Fields** | Implement an extension mechanism allowing client-specific custom fields (prefixed with "Z") on standard tables without altering the core schema. | ⬜ Not Started |
| 2.5 | **In-Memory Caching for Master Data** | Integrate an in-memory cache (e.g., Caffeine or Redis) for frequently accessed, rarely changing master data to reduce database load. | ⬜ Not Started |
| 2.6 | **Encryption of Critical Data** | Encrypt sensitive information (patient data, financial records) at the application level before storage using AES-256 or equivalent, ensuring confidentiality even with direct DB access. | ⬜ Not Started |

---

## Phase 3 — Core Framework, Data Types & System Variables

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 3.1 | **Elementary Data Types** | Define ABAP-inspired elementary types (CHAR, NUMC, DATS, TIMS, INT, DEC, CURR, QUAN, etc.) mapped to valid Java variable types and PostgreSQL column types. | ⬜ Not Started |
| 3.2 | **Complex Data Types** | Implement complex/structured data types (structures, internal tables/collections) that can be composed from elementary types. | ⬜ Not Started |
| 3.3 | **Reference Data Types** | Implement reference types for object handles, data references, and associations between entities. | ⬜ Not Started |
| 3.4 | **System Variables** | Create a globally accessible, read-only set of system variables (e.g., SY-DATUM, SY-UZEIT, SY-UNAME, SY-LANGU, SY-MANDT) available across all application layers. | ⬜ Not Started |
| 3.5 | **SAP-Style Number Ranges** | Build a Number Range management service supporting buffered and non-buffered number assignment with interval maintenance, used as part of the business logic framework. | ⬜ Not Started |
| 3.6 | **Business Logic Framework** | Establish the core framework for business rules, validations, substitutions, and event-driven processing modeled after SAP's application server. | ⬜ Not Started |

---

## Phase 4 — Security, Authentication & User Management

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 4.1 | **LDAP Integration** | Integrate with LDAP/Active Directory for centralised user directory, authentication, and organisational unit management. | ⬜ Not Started |
| 4.2 | **User, Role & Authorisation Management** | Build user management screens with role-based access control (RBAC), authorisation objects, and profile assignments similar to SAP SU01/PFCG. | ⬜ Not Started |
| 4.3 | **Two-Factor Authentication (2FA)** | Support TOTP-based 2FA with authenticator apps (Google Authenticator, Microsoft Authenticator, etc.). | ⬜ Not Started |
| 4.4 | **Passkeys & Hardware Keys** | Implement WebAuthn/FIDO2 support for passkey-based passwordless login and hardware security key authentication (e.g., YubiKey). | ⬜ Not Started |
| 4.5 | **Single Sign-On (SSO)** | Enable SSO via SAML 2.0 and/or OpenID Connect to allow users to authenticate once and access multiple system components. | ⬜ Not Started |
| 4.6 | **Login Policies** | Allow administrators to define and enforce login policies (password complexity, expiry, lockout, session timeout, IP restrictions) at user, group, and organisational unit levels. | ⬜ Not Started |
| 4.7 | **Extensible Authentication Framework** | Design the authentication layer to be extensible for future standards and methods without requiring core changes. | ⬜ Not Started |

---

## Phase 5 — API & Communication Layer

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 5.1 | **RESTful Web Services** | Expose all business functionality via secure, versioned REST APIs over HTTPS with OAuth 2.0 / JWT authentication. | ⬜ Not Started |
| 5.2 | **WebSocket Support** | Implement WebSocket endpoints for real-time data push (e.g., notifications, live dashboards, collaborative editing). | ⬜ Not Started |
| 5.3 | **API Documentation** | Auto-generate interactive API documentation using OpenAPI/Swagger, enabling custom application and client report development. | ⬜ Not Started |
| 5.4 | **API Gateway & Rate Limiting** | Introduce an API gateway for routing, throttling, rate limiting, and request/response transformation. | ⬜ Not Started |
| 5.5 | **Equal-Citizen Client Support** | Ensure all APIs are client-agnostic so that web, mobile, tablet, and third-party applications are equal citizens with identical capabilities. | ⬜ Not Started |

---

## Phase 6 — Presentation Layer & Navigation

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 6.1 | **Angular Web Application Shell** | Build the Angular application shell with theming, internationalisation (i18n), and responsive layout. | ⬜ Not Started |
| 6.2 | **T-Code Style Navigation** | Implement SAP T-code style shortcuts — a command field allowing users to type short codes to navigate directly to screens (preferably 4-character codes for frequent screens, longer codes permitted). | ⬜ Not Started |
| 6.3 | **Consistent Naming Template** | Define and enforce a naming convention for all screens, components, APIs, and T-codes to maintain uniformity across the system. | ⬜ Not Started |
| 6.4 | **System Administration Screens** | Develop administration screens for database management, number range management, user management, authorisation management, and system configuration. | ⬜ Not Started |
| 6.5 | **Favourites, Recent & Personalisation** | Allow users to save favourite T-codes, view recent navigation history, and personalise their home screen and layout. | ⬜ Not Started |

---

## Phase 7 — Mobile & Tablet Applications

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 7.1 | **Mobile App Framework Selection** | Evaluate and select a cross-platform framework (e.g., Flutter, React Native, or native) for mobile and tablet applications. | ⬜ Not Started |
| 7.2 | **Mobile App for Specialised Workflows** | Develop mobile applications for field-specific workflows (e.g., inventory scanning, patient check-in, on-site inspections). | ⬜ Not Started |
| 7.3 | **Tablet App for Specialised Workflows** | Develop tablet-optimised applications for workflows benefiting from larger screens (e.g., clinical dashboards, reporting, approvals). | ⬜ Not Started |
| 7.4 | **Offline Capability & Sync** | Enable offline data capture and automatic synchronisation when connectivity is restored. | ⬜ Not Started |

---

## Phase 8 — Resilience, Backup & Disaster Recovery

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 8.1 | **High Availability & Redundancy** | Design the deployment topology for high availability — active-passive or active-active clustering, load balancing, and failover for application and database tiers. | ⬜ Not Started |
| 8.2 | **Automated Backup Strategy** | Implement automated full, differential, and transaction-log backups with configurable retention policies and off-site replication. | ⬜ Not Started |
| 8.3 | **Disaster Recovery Plan** | Define RPO/RTO targets, set up cross-region/cross-site replication, and document recovery runbooks. | ⬜ Not Started |
| 8.4 | **Continuity of Service** | Implement health checks, circuit breakers, graceful degradation, and automated recovery mechanisms to ensure uninterrupted service. | ⬜ Not Started |
| 8.5 | **Monitoring, Alerting & Observability** | Integrate centralised logging, metrics collection, distributed tracing, and alerting (e.g., Prometheus, Grafana, ELK) to proactively detect and respond to issues. | ⬜ Not Started |

---

## Phase 9 — ERP Framework Finalization & Extensibility

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 9.1 | **ERP Development Environment** | Package the full framework (database abstraction, data types, number ranges, security, APIs) as a reusable ERP development platform. | ⬜ Not Started |
| 9.2 | **Plugin & Extension Architecture** | Design a plugin system allowing vertical-specific modules (One Health, manufacturing, retail, etc.) to be developed and deployed independently. | ⬜ Not Started |
| 9.3 | **Framework Documentation** | Produce comprehensive developer documentation, architecture decision records (ADRs), and onboarding guides. | ⬜ Not Started |
| 9.4 | **Multi-Vertical Readiness** | Validate the framework against at least two industry verticals (e.g., One Health + another) to confirm reusability and extensibility. | ⬜ Not Started |
| 9.5 | **Performance & Scalability Benchmarking** | Conduct load testing and performance benchmarking; establish baseline metrics and tuning guidelines for production deployments. | ⬜ Not Started |

---

## Feature Completion Process (ISO/IEC/IEEE 12207 & CMMI Level 5)

> Every milestone must follow this standardised completion process to comply with [ISO/IEC/IEEE 12207](https://en.wikipedia.org/wiki/ISO/IEC_12207) software lifecycle processes and [CMMI Level 5](https://en.wikipedia.org/wiki/Capability_Maturity_Model_Integration) optimizing practices.
> Refer to `.github/copilot-instructions.md` Sections 8–10 for the full standards and checklist.

### Required Artefacts per Milestone Completion

| # | Artefact | Description | ISO 12207 Process | CMMI Practice |
|---|----------|-------------|-------------------|---------------|
| 1 | **GitHub Issue(s)** | All work items for the milestone captured as issues with acceptance criteria. | Stakeholder Needs & Requirements | Requirements Management |
| 2 | **Design Documentation** | Architecture decisions, design rationale, and technical approach documented in ADRs, issues, or PR descriptions. | Architecture & Design | Technical Solution |
| 3 | **Source Code** | Implementation following coding standards (Sections 1–3 of copilot-instructions.md), with Javadoc on all public APIs. | Implementation | Product Integration |
| 4 | **Unit & Integration Tests** | 100% line and branch coverage for new code (JaCoCo). Tests follow Section 4 standards. | Verification | Verification |
| 5 | **Peer Review** | At least one approved PR review verifying standards compliance, test coverage, and design alignment. | Review & Audit | Process & Product Quality Assurance |
| 6 | **CI/CD Pipeline Pass** | All pipeline stages (build, test, static analysis) pass without failures. | Quality Assurance | Quantitative Process Management |
| 7 | **Acceptance Validation** | Acceptance criteria verified and documented in the PR description. | Validation | Validation |
| 8 | **Migration Scripts** | Database versioning scripts (Flyway/Liquibase) for any schema changes, with rollback support. | Transition | Configuration Management |
| 9 | **Milestone Status Update** | `MILESTONES.md` updated to reflect ✅ Completed (or 🔄 with a note if partially complete). | Configuration Management | Configuration Management |
| 10 | **Metrics & Lessons Learned** | Record test coverage %, build duration, defect count. Document lessons learned for continuous improvement. | Maintenance | Causal Analysis & Resolution |

### Milestone Completion Workflow

```
1. PLAN        → Create GitHub issues with acceptance criteria
                 Link issues to milestone
                 Set milestone status to 🔄 In Progress
                 Document design approach and risks

2. DEVELOP     → Implement following coding standards
                 Write tests (100% coverage)
                 Create migration scripts (if needed)
                 Add Javadoc documentation

3. VERIFY      → Run full test suite
                 Pass CI/CD pipeline
                 Submit PR for peer review
                 Verify acceptance criteria

4. VALIDATE    → Reviewer approves PR
                 All quality gates pass
                 No critical findings

5. TRANSITION  → Merge PR
                 Update MILESTONES.md status to ✅
                 Close related issues
                 Update README.md (if applicable)

6. IMPROVE     → Record metrics (coverage, build time, defects)
                 Document lessons learned
                 Propose process improvements (if any)
```

### Continuous Improvement Cycle (CMMI Level 5)

After each milestone completion, the team must:

1. **Analyse** — Review metrics collected during the milestone (defect density, test coverage trends, pipeline stability).
2. **Identify** — Identify root causes of any defects, delays, or process friction encountered.
3. **Improve** — Propose and implement process improvements (updated standards, new linting rules, enhanced CI checks).
4. **Verify** — Confirm that improvements are effective in subsequent milestones.
5. **Document** — Update `.github/copilot-instructions.md` and this file with any process changes.

> This cycle ensures that each completed milestone contributes not only to the product but also to the maturity and efficiency of the development process itself.

---

## Milestone Dependency Overview

```
Phase 1 ──► Phase 2 ──► Phase 3 ──┐
                                   ├──► Phase 5 ──► Phase 6 ──► Phase 7
              Phase 4 ─────────────┘        │
                                            ▼
                                       Phase 8
                                            │
                                            ▼
                                       Phase 9
```

> **Phase 1** (Foundation) is a prerequisite for all other phases.
> **Phases 2, 3, 4** can progress in parallel once Phase 1 is complete.
> **Phase 5** (APIs) depends on Phases 2–4 being substantially complete.
> **Phases 6 & 7** (Presentation) depend on Phase 5.
> **Phase 8** (Resilience) can begin in parallel with Phase 5 but should be validated end-to-end before Phase 9.
> **Phase 9** (Finalization) consolidates all prior phases into the reusable ERP framework.
