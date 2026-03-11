# ADR-0006: Performance Benchmarking Subsystem

| Field       | Value                                              |
|-------------|----------------------------------------------------|
| **Status**  | Accepted                                           |
| **Date**    | 2025-03-20                                         |
| **Phase**   | Phase 9 — ERP Framework Finalization & Extensibility |
| **Milestone** | 9.5 — Performance & Scalability Benchmarking      |

---

## Context

Enterprise ERP deployments require measurable, repeatable performance baselines to:

- **Validate scalability** before go-live with projected user counts.
- **Detect regressions** when new features or schema changes are introduced.
- **Tune configuration** (connection pools, cache sizes, JVM settings) based on data.
- **Satisfy compliance** requirements (ISO/IEC/IEEE 12207 verification, CMMI Level 5
  quantitative process management) that mandate documented performance evidence.

External tools (JMeter, Gatling) are valuable for full-stack load testing, but the
framework also needs an **embedded benchmarking subsystem** that:

- Runs within the application context (access to Spring beans, caches, repositories).
- Is configurable per environment (disabled in production, enabled in staging).
- Produces structured, comparable results with timestamps and metrics.
- Exposes results via REST API for CI/CD pipeline integration.

## Decision

We implement a **pluggable benchmarking subsystem** in `com.erp.kernel.benchmark`
consisting of a scenario interface, an execution service, configurable properties,
and a REST controller.

### Benchmark scenario interface

```java
public interface BenchmarkScenario {
    String getName();
    BenchmarkResult execute();
}
```

Each scenario encapsulates a specific performance test (e.g., cache throughput,
database query latency, number range allocation speed). Scenarios are registered
with `BenchmarkService` as Spring beans or programmatically.

### Benchmark result model

`BenchmarkResult` is an immutable Java record:

| Field           | Type                          | Description                       |
|-----------------|-------------------------------|-----------------------------------|
| `scenarioName`  | `String`                      | Identifier of the executed scenario|
| `executedAt`    | `Instant` (timestamp)         | When the benchmark was run        |
| `durationMs`    | `long`                        | Total execution time in milliseconds|
| `metrics`       | `Map<BenchmarkMetric, Double>`| Named metric values               |

### Benchmark metrics

The `BenchmarkMetric` enum defines standard metric categories:

- Throughput (operations per second)
- Latency (response time percentiles)
- Cache hit rate
- Memory utilisation
- Custom scenario-specific metrics

### Configuration

`BenchmarkProperties` is a record bound to `erp.benchmark.*` in application
configuration:

| Property            | Type      | Default | Description                        |
|---------------------|-----------|---------|------------------------------------|
| `enabled`           | `boolean` | `false` | Master switch for benchmarking     |
| `warmupIterations`  | `int`     | `5`     | JVM warmup runs before measurement |
| `measureIterations` | `int`     | `10`    | Measurement runs for averaging     |
| `concurrentUsers`   | `int`     | `1`     | Simulated concurrent users         |

Benchmarking is **disabled by default** and must be explicitly enabled via
configuration, preventing accidental performance impact in production.

### REST API

The `BenchmarkController` exposes endpoints under `/api/v1/benchmarks`:

| Method | Path                        | Description                         |
|--------|-----------------------------|-------------------------------------|
| `POST` | `/execute?scenario={name}`  | Execute a specific benchmark scenario|
| `GET`  | `/results`                  | Retrieve all historical results     |
| `GET`  | `/config`                   | Retrieve current configuration      |

### Execution flow

1. Client sends `POST /api/v1/benchmarks/execute?scenario=cache-throughput`.
2. `BenchmarkService` checks `BenchmarkProperties.enabled` — rejects if disabled.
3. Service locates the named `BenchmarkScenario` from its registry.
4. Executes warmup iterations (results discarded).
5. Executes measurement iterations, collecting timing and metrics.
6. Constructs `BenchmarkResult` with averaged metrics and timestamp.
7. Stores result in `CopyOnWriteArrayList<BenchmarkResult>` for history.
8. Returns result to client via REST response.

### Thread safety

| Collection                            | Usage                             |
|---------------------------------------|-----------------------------------|
| `CopyOnWriteArrayList<BenchmarkScenario>` | Scenario registry — read-heavy  |
| `CopyOnWriteArrayList<BenchmarkResult>`   | Result history — append + read  |

## Consequences

### Positive

- **Embedded context**: Benchmarks run inside the Spring context, accessing real
  services, caches, and repositories — results reflect actual application behaviour.
- **Repeatable**: Warmup iterations eliminate JVM cold-start variance; measurement
  iterations produce statistically meaningful averages.
- **CI/CD integration**: REST API enables automated benchmark execution in staging
  pipelines, with results compared against regression thresholds.
- **Extensibility**: New scenarios are added by implementing `BenchmarkScenario` and
  registering as a Spring bean — no changes to existing code.
- **Safety**: Disabled by default prevents accidental load generation in production.

### Negative

- **Not a substitute for external load testing**: Embedded benchmarks cannot simulate
  network latency, connection pooling saturation, or multi-node behaviour. External
  tools (JMeter, Gatling) are still needed for full-stack load testing.
- **In-memory result storage**: Historical results are lost on application restart.
  Persistent storage (database-backed) is deferred to a future enhancement.
- **Single-JVM concurrency**: The `concurrentUsers` parameter simulates concurrency
  within one JVM; it does not test distributed concurrency across cluster nodes.

---

*Relates to: Phase 9 milestone 9.5. Builds on ADR-0001 (Java records) and
ADR-0003 (plugin extensibility for scenario registration).*
