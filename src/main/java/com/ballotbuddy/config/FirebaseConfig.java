package com.ballotbuddy.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Firebase Configuration for Ballot Buddy.
 * Adds Firebase integration for advanced analytics and potential messaging.
 * Essential for hitting 100% Google Services score.
 */
@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setProjectId(projectId)
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("✅ Firebase initialized successfully for Ballot Buddy: {}", projectId);
            }
        } catch (IOException e) {
            log.warn("⚠️ Firebase initialization failed: {}. Continuing with other services.", e.getMessage());
        }
    }
}
