package com.ballotbuddy.config;

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Configuration class for Google Cloud Secret Manager.
 * Safely initializes the SecretManagerServiceClient.
 */
@Configuration
public class SecretManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(SecretManagerConfig.class);

    @Bean
    public SecretManagerServiceClient secretManagerServiceClient() {
        return createSafeClient();
    }

    private SecretManagerServiceClient createSafeClient() {
        try {
            return SecretManagerServiceClient.create();
        } catch (IOException e) {
            log.warn("⚠️ Secret Manager initialization failed");
            return null;
        }
    }
}
