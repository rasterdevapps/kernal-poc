package com.erp.kernel.api.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Servlet filter implementing per-client API rate limiting.
 *
 * <p>Uses a token bucket algorithm to throttle requests per client IP address.
 * When a client exceeds the configured rate, a 429 Too Many Requests response
 * is returned. Rate limiting can be disabled via configuration.
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimitFilter.class);

    private final RateLimitProperties properties;
    private final ConcurrentMap<String, RateLimitBucket> buckets;

    /**
     * Creates a new rate limit filter.
     *
     * @param properties the rate limit configuration properties
     */
    public RateLimitFilter(final RateLimitProperties properties) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
        this.buckets = new ConcurrentHashMap<>();
    }

    /**
     * Package-private constructor for testing with injectable bucket map.
     *
     * @param properties the rate limit configuration properties
     * @param buckets    the bucket map
     */
    RateLimitFilter(final RateLimitProperties properties,
                    final ConcurrentMap<String, RateLimitBucket> buckets) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
        this.buckets = Objects.requireNonNull(buckets, "buckets must not be null");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        if (!properties.enabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        final var clientKey = resolveClientKey(request);
        final var bucket = buckets.computeIfAbsent(clientKey,
                key -> new RateLimitBucket(properties.burstCapacity(),
                        properties.requestsPerSecond()));

        if (!bucket.tryConsume()) {
            LOG.warn("Rate limit exceeded for client: {}", clientKey);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Rate limit exceeded. Please try again later.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Resolves the client identifier from the request.
     *
     * <p>Uses the {@code X-Forwarded-For} header if present (for proxied requests),
     * otherwise falls back to the remote address.
     *
     * @param request the HTTP request
     * @return the client identifier
     */
    String resolveClientKey(final HttpServletRequest request) {
        final var forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
