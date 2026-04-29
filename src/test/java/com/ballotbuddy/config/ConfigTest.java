package com.ballotbuddy.config;

import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigTest {

    @Mock
    private StateElectionRepository repository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void dataInitializer_InitializesStates() throws Exception {
        when(repository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        dataInitializer.run();

        verify(repository, times(1)).saveAll(argThat(list -> {
            List<StateElection> states = (List<StateElection>) list;
            return states.size() == 4 &&
                   states.stream().anyMatch(s -> "Maharashtra".equals(s.getStateName())) &&
                   states.stream().anyMatch(s -> "Uttar Pradesh".equals(s.getStateName())) &&
                   states.stream().anyMatch(s -> "Karnataka".equals(s.getStateName())) &&
                   states.stream().anyMatch(s -> "Delhi".equals(s.getStateName()));
        }));
    }

    @Test
    void securityHeadersFilter_AddsSecurityHeaders() throws IOException, ServletException {
        SecurityHeadersFilter filter = new SecurityHeadersFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals("DENY", response.getHeader("X-Frame-Options"));
        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("1; mode=block", response.getHeader("X-XSS-Protection"));
        assertNotNull(response.getHeader("Content-Security-Policy"));
        assertTrue(response.getHeader("Content-Security-Policy").contains("default-src 'self'"));
    }

    @Test
    void webConfig_ConfiguresCors() {
        WebConfig webConfig = new WebConfig();

        // Simply verify that the method can be called without exceptions
        // The actual CORS configuration is tested via integration tests
        assertDoesNotThrow(() -> {
            CorsRegistry registry = new CorsRegistry();
            webConfig.addCorsMappings(registry);
        });
    }
}
