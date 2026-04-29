package com.ballotbuddy.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

class SecurityHeadersFilterTest {

    @Test
    void doFilter_ShouldAddHeaders() throws IOException, ServletException {
        SecurityHeadersFilter filter = new SecurityHeadersFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(response).setHeader("X-Frame-Options", "DENY");
        verify(response).setHeader("X-Content-Type-Options", "nosniff");
        verify(response).setHeader("X-XSS-Protection", "1; mode=block");
        verify(response, atLeastOnce()).setHeader(eq("Content-Security-Policy"), anyString());
        verify(chain).doFilter(request, response);
    }
}
