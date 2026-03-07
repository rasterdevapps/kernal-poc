package com.erp.kernel.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that adds standard response headers to ensure all clients
 * (web, mobile, tablet, third-party) receive consistent API responses.
 *
 * <p>Adds cache control, content type options, and API version headers
 * to every response for client-agnostic consumption.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientSupportFilter extends OncePerRequestFilter {

    /** Response header for API version. */
    static final String API_VERSION_HEADER = "X-API-Version";

    /** Current API version. */
    static final String API_VERSION = "1.0.0";

    /** Response header indicating content type options. */
    static final String CONTENT_TYPE_OPTIONS_HEADER = "X-Content-Type-Options";

    /** Value preventing MIME type sniffing. */
    static final String NOSNIFF = "nosniff";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader(API_VERSION_HEADER, API_VERSION);
        response.setHeader(CONTENT_TYPE_OPTIONS_HEADER, NOSNIFF);
        response.setHeader("Cache-Control", "no-store");
        filterChain.doFilter(request, response);
    }
}
