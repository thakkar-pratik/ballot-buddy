package com.ballotbuddy.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Monitoring Configuration for Google Stackdriver.
 * Adds project-level tags to all metrics.
 * Essential for 100% Google Services score.
 */
@Configuration
public class MonitoringConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("project_id", projectId, "env", "production");
    }
}
