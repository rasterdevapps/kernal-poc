package com.erp.kernel.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures OpenAPI/Swagger documentation for the ERP Kernel API.
 *
 * <p>Auto-generates interactive API documentation accessible at
 * {@code /swagger-ui/index.html} with JWT Bearer authentication support.
 */
@Configuration
public class OpenApiConfig {

    /** The security scheme name for JWT Bearer authentication. */
    static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Creates the OpenAPI specification with metadata and security configuration.
     *
     * @return the configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI erpKernelOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ERP Kernel API")
                        .version("1.0.0")
                        .description("RESTful API for the ERP Kernel platform providing "
                                + "data dictionary, number range, security, and business logic services.")
                        .contact(new Contact()
                                .name("ERP Kernel Team")
                                .email("support@erp-kernel.dev"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://erp-kernel.dev/license")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token authentication. "
                                        + "Obtain a token via POST /api/v1/auth/token")));
    }
}
