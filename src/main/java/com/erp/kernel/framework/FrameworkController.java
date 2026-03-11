package com.erp.kernel.framework;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

/**
 * REST controller for ERP framework information.
 *
 * <p>Provides endpoints for querying framework metadata and registered
 * modules at {@code /api/v1/framework}.
 */
@RestController
@RequestMapping("/api/v1/framework")
public class FrameworkController {

    private final FrameworkInfoService frameworkInfoService;
    private final ErpModuleRegistry moduleRegistry;

    /**
     * Creates a new framework controller.
     *
     * @param frameworkInfoService the framework info service
     * @param moduleRegistry      the module registry
     */
    public FrameworkController(final FrameworkInfoService frameworkInfoService,
                               final ErpModuleRegistry moduleRegistry) {
        this.frameworkInfoService = Objects.requireNonNull(
                frameworkInfoService, "frameworkInfoService must not be null");
        this.moduleRegistry = Objects.requireNonNull(
                moduleRegistry, "moduleRegistry must not be null");
    }

    /**
     * Returns the framework metadata including name, version, and capabilities.
     *
     * @return the framework info
     */
    @GetMapping("/info")
    public ResponseEntity<FrameworkInfo> getFrameworkInfo() {
        return ResponseEntity.ok(frameworkInfoService.getFrameworkInfo());
    }

    /**
     * Returns all registered ERP modules.
     *
     * @return the registered modules
     */
    @GetMapping("/modules")
    public ResponseEntity<Collection<ErpModule>> getModules() {
        return ResponseEntity.ok(moduleRegistry.getAll());
    }
}
