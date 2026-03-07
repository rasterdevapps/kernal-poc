package com.erp.kernel.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes a basic health-check endpoint for the ERP Kernel.
 *
 * <p>This controller is independent of Spring Boot Actuator and provides
 * a lightweight, application-level liveness probe at {@code /api/v1/health}.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    /**
     * Returns a simple status message indicating the application is running.
     *
     * @return a {@link ResponseEntity} containing the status string {@code "UP"}
     */
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }
}
