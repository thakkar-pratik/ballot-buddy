package com.ballotbuddy.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirebaseAuthConfigurationTest {

    @Test
    void firebaseAuth_BeanCreation() {
        try (MockedStatic<FirebaseAuth> mockedAuth = mockStatic(FirebaseAuth.class);
             MockedStatic<FirebaseApp> mockedApp = mockStatic(FirebaseApp.class)) {
            
            FirebaseAuth mockAuth = mock(FirebaseAuth.class);
            mockedAuth.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            
            FirebaseAuthConfiguration config = new FirebaseAuthConfiguration();
            FirebaseAuth result = config.firebaseAuth();
            
            assertNotNull(result);
            assertEquals(mockAuth, result);
        }
    }
}
