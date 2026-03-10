package com.erp.kernel.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OpenApiConfig}.
 */
class OpenApiConfigTest {

    private final OpenApiConfig openApiConfig = new OpenApiConfig();

    @Test
    void shouldCreateOpenApiBean_whenCalled() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi).isNotNull();
        assertThat(openApi).isInstanceOf(OpenAPI.class);
    }

    @Test
    void shouldSetApiTitle_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getTitle()).isEqualTo("ERP Kernel API");
    }

    @Test
    void shouldSetApiVersion_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getVersion()).isEqualTo("1.0.0");
    }

    @Test
    void shouldSetApiDescription_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getDescription())
                .startsWith("RESTful API for the ERP Kernel platform");
    }

    @Test
    void shouldSetContactName_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getContact().getName()).isEqualTo("ERP Kernel Team");
    }

    @Test
    void shouldSetContactEmail_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getContact().getEmail()).isEqualTo("support@erp-kernel.dev");
    }

    @Test
    void shouldSetLicenseName_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getLicense().getName()).isEqualTo("Proprietary");
    }

    @Test
    void shouldSetLicenseUrl_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getInfo().getLicense().getUrl()).isEqualTo("https://erp-kernel.dev/license");
    }

    @Test
    void shouldConfigureSecurityScheme_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();
        final var securityScheme = openApi.getComponents()
                .getSecuritySchemes()
                .get(OpenApiConfig.SECURITY_SCHEME_NAME);

        // Assert
        assertThat(securityScheme).isNotNull();
        assertThat(securityScheme.getName()).isEqualTo("bearerAuth");
        assertThat(securityScheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(securityScheme.getScheme()).isEqualTo("bearer");
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
    }

    @Test
    void shouldConfigureSecuritySchemeDescription_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();
        final var securityScheme = openApi.getComponents()
                .getSecuritySchemes()
                .get(OpenApiConfig.SECURITY_SCHEME_NAME);

        // Assert
        assertThat(securityScheme.getDescription())
                .contains("JWT Bearer token authentication");
    }

    @Test
    void shouldAddSecurityRequirement_whenBeanCreated() {
        // Act
        final var openApi = openApiConfig.erpKernelOpenApi();

        // Assert
        assertThat(openApi.getSecurity()).isNotNull();
        assertThat(openApi.getSecurity()).hasSize(1);
        assertThat(openApi.getSecurity().getFirst().containsKey(OpenApiConfig.SECURITY_SCHEME_NAME)).isTrue();
    }

    @Test
    void shouldHaveCorrectSecuritySchemeName() {
        // Assert
        assertThat(OpenApiConfig.SECURITY_SCHEME_NAME).isEqualTo("bearerAuth");
    }
}
