package com.ballotbuddy.config;

import com.google.cloud.logging.Logging;
import com.google.cloud.storage.Storage;
import com.google.cloud.vertexai.VertexAI;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class GoogleCloudConfigTest {

    @Test
    void testBeanCreation() {
        GoogleCloudConfig config = new GoogleCloudConfig();
        ReflectionTestUtils.setField(config, "projectId", "test-project");
        ReflectionTestUtils.setField(config, "location", "us-central1");

        Storage storage = config.googleCloudStorage();
        assertNotNull(storage);

        Logging logging = config.googleCloudLogging();
        assertNotNull(logging);

        VertexAI vertexAI = config.vertexAI();
        assertNotNull(vertexAI);
    }
}
