package com.erp.kernel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test that verifies the Spring application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class KernelApplicationTest {

    @Test
    void shouldLoadApplicationContext() {
        // Verifies the application context starts without errors
    }
}
