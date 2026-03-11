package com.erp.kernel.framework;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service that provides metadata about the ERP Kernel framework.
 *
 * <p>Aggregates information about the framework version, registered modules,
 * and core capabilities into a unified {@link FrameworkInfo} response.
 */
@Service
public class FrameworkInfoService {

    private static final String FRAMEWORK_NAME = "ERP Kernel";
    private static final String FRAMEWORK_VERSION = "1.0.0";
    private static final List<String> CORE_CAPABILITIES = List.of(
            "Database Abstraction (ANSI/SPARC Three-Schema)",
            "Data Dictionary (DDIC)",
            "Elementary, Complex & Reference Data Types",
            "Number Range Management",
            "Business Logic Framework",
            "System Variables",
            "Security & Authentication (LDAP, SSO, MFA, WebAuthn)",
            "RESTful API Layer with JWT",
            "WebSocket Real-Time Communication",
            "API Gateway & Rate Limiting",
            "Encryption Services (AES-256)",
            "Circuit Breaker & Resilience",
            "Backup & Disaster Recovery",
            "Observability & Monitoring"
    );

    private final ErpModuleRegistry moduleRegistry;

    /**
     * Creates a new framework info service.
     *
     * @param moduleRegistry the ERP module registry
     */
    public FrameworkInfoService(final ErpModuleRegistry moduleRegistry) {
        this.moduleRegistry = Objects.requireNonNull(
                moduleRegistry, "moduleRegistry must not be null");
    }

    /**
     * Returns the current framework metadata.
     *
     * @return the framework info
     */
    public FrameworkInfo getFrameworkInfo() {
        return new FrameworkInfo(FRAMEWORK_NAME, FRAMEWORK_VERSION, CORE_CAPABILITIES);
    }

    /**
     * Returns the number of registered ERP modules.
     *
     * @return the module count
     */
    public int getModuleCount() {
        return moduleRegistry.getAll().size();
    }
}
