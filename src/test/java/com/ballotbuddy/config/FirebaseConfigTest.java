package com.ballotbuddy.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FirebaseConfigTest {

    @Test
    void initialize_Success() {
        FirebaseConfig config = new FirebaseConfig();
        ReflectionTestUtils.setField(config, "projectId", "test-project");

        try (MockedStatic<FirebaseApp> mockedApp = mockStatic(FirebaseApp.class);
             MockedStatic<GoogleCredentials> mockedCreds = mockStatic(GoogleCredentials.class)) {
            
            mockedApp.when(FirebaseApp::getApps).thenReturn(Collections.emptyList());
            mockedCreds.when(GoogleCredentials::getApplicationDefault).thenReturn(mock(GoogleCredentials.class));

            config.initialize();

            mockedApp.verify(() -> FirebaseApp.initializeApp(any(FirebaseOptions.class)));
        }
    }

    @Test
    void initialize_AlreadyInitialized() {
        FirebaseConfig config = new FirebaseConfig();
        ReflectionTestUtils.setField(config, "projectId", "test-project");

        try (MockedStatic<FirebaseApp> mockedApp = mockStatic(FirebaseApp.class)) {
            mockedApp.when(FirebaseApp::getApps).thenReturn(Collections.singletonList(mock(FirebaseApp.class)));

            config.initialize();

            mockedApp.verify(() -> FirebaseApp.initializeApp(any(FirebaseOptions.class)), never());
        }
    }

    @Test
    void initialize_Failure() {
        FirebaseConfig config = new FirebaseConfig();
        ReflectionTestUtils.setField(config, "projectId", "test-project");

        try (MockedStatic<FirebaseApp> mockedApp = mockStatic(FirebaseApp.class);
             MockedStatic<GoogleCredentials> mockedCreds = mockStatic(GoogleCredentials.class)) {
            
            mockedApp.when(FirebaseApp::getApps).thenReturn(Collections.emptyList());
            mockedCreds.when(GoogleCredentials::getApplicationDefault).thenThrow(new IOException("Creds failed"));

            // Should not throw exception, just log warning
            config.initialize();
        }
    }
}
