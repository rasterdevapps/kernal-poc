package com.erp.kernel.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * Web MVC configuration for CORS and content negotiation.
 *
 * <p>Ensures all APIs are client-agnostic: web, mobile, tablet, and
 * third-party applications can consume APIs equally with proper
 * CORS headers and content negotiation support.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    private final CorsProperties corsProperties;

    /**
     * Creates a new web configuration.
     *
     * @param corsProperties the CORS configuration properties
     */
    public WebConfig(final CorsProperties corsProperties) {
        this.corsProperties = Objects.requireNonNull(corsProperties, "corsProperties must not be null");
        LOG.info("Web configuration initialised with CORS origins: {}", corsProperties.allowedOrigins());
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(corsProperties.allowedOrigins().toArray(String[]::new))
                .allowedMethods(corsProperties.allowedMethods().toArray(String[]::new))
                .allowedHeaders(corsProperties.allowedHeaders().toArray(String[]::new))
                .allowCredentials(corsProperties.allowCredentials())
                .maxAge(corsProperties.maxAge());
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
    }
}
