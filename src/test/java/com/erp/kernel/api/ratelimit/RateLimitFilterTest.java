package com.erp.kernel.api.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link RateLimitFilter}.
 */
@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new RateLimitFilter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenBucketsIsNull() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);

        // Act & Assert
        assertThatThrownBy(() -> new RateLimitFilter(properties, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("buckets must not be null");
    }

    @Test
    void shouldPassThrough_whenRateLimitingIsDisabled() throws ServletException, IOException {
        // Arrange
        final var properties = new RateLimitProperties(false, 100, 200);
        final var filter = new RateLimitFilter(properties);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAllowRequest_whenTokensAreAvailable() throws ServletException, IOException {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);
        final var filter = new RateLimitFilter(properties);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldReturn429_whenRateLimitExceeded() throws ServletException, IOException {
        // Arrange
        final var properties = new RateLimitProperties(true, 10, 10);
        final var bucket = mock(RateLimitBucket.class);
        when(bucket.tryConsume()).thenReturn(false);

        final ConcurrentMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();
        buckets.put("192.168.1.1", bucket);

        final var filter = new RateLimitFilter(properties, buckets);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(429);
        verify(response).setContentType("application/json");
        printWriter.flush();
        assertThat(stringWriter.toString())
                .contains("Rate limit exceeded. Please try again later.");
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldUseXForwardedFor_whenHeaderIsPresent() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);
        final var filter = new RateLimitFilter(properties);
        when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 10.0.0.2");

        // Act
        final var clientKey = filter.resolveClientKey(request);

        // Assert
        assertThat(clientKey).isEqualTo("10.0.0.1");
    }

    @Test
    void shouldUseRemoteAddr_whenXForwardedForIsAbsent() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);
        final var filter = new RateLimitFilter(properties);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("172.16.0.1");

        // Act
        final var clientKey = filter.resolveClientKey(request);

        // Assert
        assertThat(clientKey).isEqualTo("172.16.0.1");
    }

    @Test
    void shouldUseRemoteAddr_whenXForwardedForIsBlank() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);
        final var filter = new RateLimitFilter(properties);
        when(request.getHeader("X-Forwarded-For")).thenReturn("   ");
        when(request.getRemoteAddr()).thenReturn("172.16.0.1");

        // Act
        final var clientKey = filter.resolveClientKey(request);

        // Assert
        assertThat(clientKey).isEqualTo("172.16.0.1");
    }

    @Test
    void shouldAllowRequest_whenBucketHasTokens() throws ServletException, IOException {
        // Arrange
        final var properties = new RateLimitProperties(true, 10, 10);
        final var bucket = mock(RateLimitBucket.class);
        when(bucket.tryConsume()).thenReturn(true);

        final ConcurrentMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();
        buckets.put("10.0.0.5", bucket);

        final var filter = new RateLimitFilter(properties, buckets);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.5");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(bucket).tryConsume();
    }

    @Test
    void shouldCreateNewBucket_whenClientIsNew() throws ServletException, IOException {
        // Arrange
        final var properties = new RateLimitProperties(true, 50, 100);
        final ConcurrentMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();
        final var filter = new RateLimitFilter(properties, buckets);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.2.1");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(buckets).containsKey("192.168.2.1");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldTrimXForwardedForValue_whenItContainsSpaces() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);
        final var filter = new RateLimitFilter(properties);
        when(request.getHeader("X-Forwarded-For")).thenReturn("  10.0.0.1 , 10.0.0.2");

        // Act
        final var clientKey = filter.resolveClientKey(request);

        // Assert
        assertThat(clientKey).isEqualTo("10.0.0.1");
    }
}
