package com.erp.kernel;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Unit test for the {@link KernelApplication} main method.
 */
class KernelApplicationMainTest {

    @Test
    void shouldStartMainMethodWithoutException() {
        assertThatCode(() -> KernelApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}
