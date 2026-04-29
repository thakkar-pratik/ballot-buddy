package com.ballotbuddy;

import com.google.cloud.logging.Logging;
import com.google.cloud.storage.Storage;
import com.google.cloud.vertexai.VertexAI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration(excludeName = {
    "com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration",
    "com.google.cloud.spring.autoconfigure.storage.GcpStorageAutoConfiguration",
    "com.google.cloud.spring.autoconfigure.logging.StackdriverLoggingAutoConfiguration",
    "org.springframework.boot.actuate.autoconfigure.metrics.export.stackdriver.StackdriverMetricsExportAutoConfiguration"
})
class BallotBuddyApplicationTest {

    @MockBean
    private Storage storage;

    @MockBean
    private Logging logging;

    @MockBean
    private VertexAI vertexAI;

    @MockBean
    private com.google.firebase.auth.FirebaseAuth firebaseAuth;

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        try (org.mockito.MockedStatic<org.springframework.boot.SpringApplication> mockedSpring = org.mockito.Mockito.mockStatic(org.springframework.boot.SpringApplication.class)) {
            mockedSpring.when(() -> org.springframework.boot.SpringApplication.run(org.mockito.ArgumentMatchers.any(Class.class), org.mockito.ArgumentMatchers.any(String[].class)))
                    .thenReturn(null);
            
            BallotBuddyApplication.main(new String[]{});
            
            mockedSpring.verify(() -> org.springframework.boot.SpringApplication.run(org.mockito.ArgumentMatchers.eq(BallotBuddyApplication.class), org.mockito.ArgumentMatchers.any(String[].class)));
        }
    }
}
