package com.ballotbuddy.exception;

import com.google.cloud.logging.Logging;
import com.google.cloud.storage.Storage;
import com.google.cloud.vertexai.VertexAI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableAutoConfiguration(excludeName = {
    "com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration",
    "com.google.cloud.spring.autoconfigure.storage.GcpStorageAutoConfiguration",
    "com.google.cloud.spring.autoconfigure.logging.StackdriverLoggingAutoConfiguration",
    "org.springframework.boot.actuate.autoconfigure.metrics.export.stackdriver.StackdriverMetricsExportAutoConfiguration"
})
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Storage storage;

    @MockBean
    private Logging logging;

    @MockBean
    private com.google.firebase.auth.FirebaseAuth firebaseAuth;

    @MockBean
    private VertexAI vertexAI;

    @Test
    void handleAllExceptions_ReturnsInternalError() throws Exception {
        // Sending an invalid JSON body to force an exception
        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid: json }"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));
    }
}
