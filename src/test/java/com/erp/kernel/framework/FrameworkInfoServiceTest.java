package com.erp.kernel.framework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link FrameworkInfoService}.
 */
@ExtendWith(MockitoExtension.class)
class FrameworkInfoServiceTest {

    @Mock
    private ErpModuleRegistry moduleRegistry;

    @InjectMocks
    private FrameworkInfoService frameworkInfoService;

    @Test
    void shouldReturnFrameworkInfo_whenGetFrameworkInfoCalled() {
        // Act
        var info = frameworkInfoService.getFrameworkInfo();

        // Assert
        assertThat(info).isNotNull();
        assertThat(info.name()).isEqualTo("ERP Kernel");
        assertThat(info.version()).isEqualTo("1.0.0");
        assertThat(info.capabilities()).isNotEmpty();
        assertThat(info.capabilities()).contains(
                "Database Abstraction (ANSI/SPARC Three-Schema)",
                "Data Dictionary (DDIC)",
                "Security & Authentication (LDAP, SSO, MFA, WebAuthn)",
                "RESTful API Layer with JWT");
    }

    @Test
    void shouldReturnModuleCount_whenModulesAreRegistered() {
        // Arrange
        var modules = List.<ErpModule>of(
                ErpModuleDescriptor.of("core", "1.0.0", "Core"),
                ErpModuleDescriptor.of("ddic", "1.0.0", "DDIC"));
        when(moduleRegistry.getAll()).thenReturn(modules);

        // Act
        var count = frameworkInfoService.getModuleCount();

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldReturnZero_whenNoModulesRegistered() {
        // Arrange
        when(moduleRegistry.getAll()).thenReturn(List.of());

        // Act
        var count = frameworkInfoService.getModuleCount();

        // Assert
        assertThat(count).isZero();
    }

    @Test
    void shouldThrowNullPointerException_whenRegistryIsNull() {
        assertThatThrownBy(() -> new FrameworkInfoService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("moduleRegistry must not be null");
    }
}
