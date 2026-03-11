# ADR-0003: Plugin and Extension Point Architecture

| Field       | Value                                              |
|-------------|----------------------------------------------------|
| **Status**  | Accepted                                           |
| **Date**    | 2025-03-01                                         |
| **Phase**   | Phase 9 — ERP Framework Finalization & Extensibility |
| **Milestone** | 9.2 — Plugin & Extension Architecture             |

---

## Context

The ERP Kernel must serve as a reusable platform for multiple industry verticals
(healthcare, manufacturing, retail, finance, education). Each vertical introduces
domain-specific modules that must be developed, deployed, and upgraded independently
without modifying the core framework.

Requirements for the plugin system:

- **Independent lifecycle**: Plugins must be initialised, started, stopped, and
  destroyed without affecting other plugins or the core framework.
- **Dependency management**: Plugins may declare dependencies on other plugins.
- **Extension contributions**: Plugins must be able to contribute functionality at
  well-defined extension points in the core framework.
- **Thread safety**: The plugin registry must support concurrent registration and
  lookup in a multi-threaded Spring Boot environment.
- **Discoverability**: Plugins must be queryable at runtime via REST API.

## Decision

We implement a **plugin system with managed lifecycle states** and a **typed extension
point registry**, located in `com.erp.kernel.plugin`.

### Plugin lifecycle

Plugins transition through five ordered states defined by the `PluginState` enum:

```
CREATED → INITIALIZED → STARTED → STOPPED → DESTROYED
```

| State         | Trigger               | Description                                |
|---------------|-----------------------|--------------------------------------------|
| `CREATED`     | Registration          | Plugin registered, no resources allocated   |
| `INITIALIZED` | `Plugin.initialize()` | Configuration loaded, dependencies resolved |
| `STARTED`     | `Plugin.start()`      | Actively processing, extensions registered  |
| `STOPPED`     | `Plugin.stop()`       | Gracefully paused, extensions unregistered  |
| `DESTROYED`   | `Plugin.destroy()`    | All resources released, terminal state      |

### Core interfaces and components

| Component              | Type        | Responsibility                                  |
|------------------------|-------------|--------------------------------------------------|
| `Plugin`               | Interface   | Lifecycle contract: `initialize`, `start`, `stop`, `destroy` |
| `PluginState`          | Enum        | Five lifecycle states with ordering              |
| `PluginDescriptor`     | Record      | Immutable metadata: id, name, version, dependencies, state |
| `PluginContext`        | Class       | Runtime context passed during initialisation     |
| `PluginRegistry`       | Component   | Thread-safe registry backed by `ConcurrentHashMap` |
| `ExtensionPoint<T>`    | Interface   | Generic typed extension point contract           |
| `ExtensionRegistry`    | Component   | Extension contributions indexed by point name    |
| `PluginController`     | Controller  | REST API for plugin management and inspection    |

### Thread safety strategy

| Collection                        | Usage                              |
|-----------------------------------|------------------------------------|
| `ConcurrentHashMap<String, ...>`  | Plugin registry — concurrent R/W   |
| `CopyOnWriteArrayList<Object>`    | Extension lists — read-heavy       |
| `ConcurrentMap<String, List<…>>`  | Extension registry — concurrent R/W|

The `ConcurrentHashMap` provides lock-free reads and segmented writes, suitable for a
registry that is written to during startup and read frequently at runtime. Extension
lists use `CopyOnWriteArrayList` because extensions are registered once (during plugin
start) and read many times during request processing.

### Extension point pattern

```java
// Core framework defines an extension point
public interface ExtensionPoint<T> {
    String getName();
    Class<T> getType();
}

// Plugin contributes an extension
extensionRegistry.register("menu-items", myMenuExtension);

// Core framework queries extensions
List<Object> extensions = extensionRegistry.getExtensions("menu-items");
```

## Consequences

### Positive

- **Vertical independence**: Industry verticals are packaged as plugins that can be
  added or removed without rebuilding the core framework.
- **Safe upgrades**: Plugin lifecycle states enable graceful shutdown and restart during
  hot deployments.
- **Extensibility without modification**: Extension points follow the Open/Closed
  Principle — the core framework is open for extension but closed for modification.
- **Runtime introspection**: The REST API allows administrators to inspect plugin
  status, dependencies, and registered extensions.
- **Thread safety**: `ConcurrentHashMap` and `CopyOnWriteArrayList` eliminate
  synchronisation bottlenecks in concurrent environments.

### Negative

- **Complexity**: The lifecycle state machine adds complexity compared to simple Spring
  component scanning. Mitigated by clear state transition documentation.
- **No hot-loading**: Plugins are registered at application startup; true hot-deploy
  (adding plugins without restart) is not yet supported.
- **Type erasure**: Java generics in `ExtensionPoint<T>` are erased at runtime,
  requiring explicit `Class<T>` tokens for type safety.

---

*Relates to: Phase 9 milestones 9.1–9.2. Builds on ADR-0001 (Java 25 features).*
