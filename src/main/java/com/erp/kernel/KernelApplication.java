package com.erp.kernel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Main entry point for the ERP Kernel application.
 *
 * <p>Bootstraps the Spring Boot application context and starts the embedded server.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class KernelApplication {

    /**
     * Starts the ERP Kernel application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(KernelApplication.class, args);
    }
}
