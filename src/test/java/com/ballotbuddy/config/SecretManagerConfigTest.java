package com.ballotbuddy.config;

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecretManagerConfigTest {

    @Test
    void secretManagerServiceClient_Success() {
        try (MockedStatic<SecretManagerServiceClient> mockedClient = mockStatic(SecretManagerServiceClient.class)) {
            SecretManagerServiceClient mock = mock(SecretManagerServiceClient.class);
            mockedClient.when(SecretManagerServiceClient::create).thenReturn(mock);
            
            SecretManagerConfig config = new SecretManagerConfig();
            SecretManagerServiceClient result = config.secretManagerServiceClient();
            
            assertNotNull(result);
            assertEquals(mock, result);
        }
    }

    @Test
    void secretManagerServiceClient_Failure() {
        try (MockedStatic<SecretManagerServiceClient> mockedClient = mockStatic(SecretManagerServiceClient.class)) {
            mockedClient.when(SecretManagerServiceClient::create).thenThrow(new IOException("GCP Error"));
            
            SecretManagerConfig config = new SecretManagerConfig();
            SecretManagerServiceClient result = config.secretManagerServiceClient();
            
            assertNull(result);
        }
    }

    @Test
    void testConstructor() {
        assertNotNull(new SecretManagerConfig());
    }
}
