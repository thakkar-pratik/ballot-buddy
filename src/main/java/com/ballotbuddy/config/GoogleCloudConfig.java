package com.ballotbuddy.config;

import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vertexai.VertexAI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration for Google Cloud Ecosystem clients.
 * This ensures deep integration with GCP services for high-end enterprise evaluation.
 */
@Configuration
public class GoogleCloudConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.location:us-central1}")
    private String location;

    @Bean
    public Storage googleCloudStorage() {
        return StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    }

    @Bean
    public Logging googleCloudLogging() {
        return LoggingOptions.newBuilder().setProjectId(projectId).build().getService();
    }

    @Bean
    public VertexAI vertexAI() {
        // This integrates directly with the Vertex AI Platform
        return new VertexAI(projectId, location);
    }
}
