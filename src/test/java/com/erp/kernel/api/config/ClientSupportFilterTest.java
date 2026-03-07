package com.erp.kernel.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ClientSupportFilter}.
 */
@ExtendWith(MockitoExtension.class)
class ClientSupportFilterTest {

    @InjectMocks
    private ClientSupportFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void shouldSetApiVersionHeader_whenFilterExecuted() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setHeader(ClientSupportFilter.API_VERSION_HEADER, ClientSupportFilter.API_VERSION);
    }

    @Test
    void shouldSetContentTypeOptionsHeader_whenFilterExecuted() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setHeader(ClientSupportFilter.CONTENT_TYPE_OPTIONS_HEADER, ClientSupportFilter.NOSNIFF);
    }

    @Test
    void shouldSetCacheControlHeader_whenFilterExecuted() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setHeader("Cache-Control", "no-store");
    }

    @Test
    void shouldInvokeFilterChain_whenFilterExecuted() throws ServletException, IOException {
        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldHaveCorrectConstantValues_whenChecked() {
        // Assert
        assertThat(ClientSupportFilter.API_VERSION_HEADER).isEqualTo("X-API-Version");
        assertThat(ClientSupportFilter.API_VERSION).isEqualTo("1.0.0");
        assertThat(ClientSupportFilter.CONTENT_TYPE_OPTIONS_HEADER).isEqualTo("X-Content-Type-Options");
        assertThat(ClientSupportFilter.NOSNIFF).isEqualTo("nosniff");
    }
}
