package com.ballotbuddy.service;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.Severity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudLoggingServiceTest {

    @Mock
    private Logging logging;

    @InjectMocks
    private CloudLoggingService cloudLoggingService;

    @Test
    void log_Success() {
        cloudLoggingService.log("Test message", Severity.INFO);
        verify(logging, times(1)).write(anyIterable());
    }

    @Test
    void log_ExceptionHandled() {
        doThrow(new RuntimeException("GCP Down")).when(logging).write(anyIterable());
        
        // Should not throw exception
        assertDoesNotThrow(() -> cloudLoggingService.log("Test message", Severity.ERROR));
        verify(logging, times(1)).write(anyIterable());
    }

    private void assertDoesNotThrow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            org.junit.jupiter.api.Assertions.fail("Should not have thrown exception: " + e.getMessage());
        }
    }
}
