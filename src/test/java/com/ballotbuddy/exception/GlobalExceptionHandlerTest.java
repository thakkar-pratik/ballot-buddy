package com.ballotbuddy.exception;

import com.ballotbuddy.controller.ChatController;
import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.service.AnalyticsStorageService;
import com.ballotbuddy.service.CloudLoggingService;
import com.ballotbuddy.service.GeminiApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeminiApiService geminiApiService;

    @MockBean
    private AnalyticsStorageService analyticsStorageService;

    @MockBean
    private CloudLoggingService cloudLoggingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleValidationExceptions_ReturnsBadRequest() throws Exception {
        // Sending blank query to trigger validation error
        ChatRequest request = new ChatRequest("");

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details.query").exists());
    }

    @Test
    void handleAllExceptions_ReturnsInternalServerError() throws Exception {
        ChatRequest request = new ChatRequest("Test question");
        
        // Mock service to throw a runtime exception
        when(geminiApiService.askQuestion(any())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected internal error occurred"));
    }

    @Test
    void handleAllExceptions_VertexAIError() throws Exception {
        ChatRequest request = new ChatRequest("Test question");
        when(geminiApiService.askQuestion(any())).thenThrow(new RuntimeException("VertexAI service error"));

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("AI_SERVICE_ERROR"))
                .andExpect(jsonPath("$.message").value("AI service is currently unavailable or ratelimited."));
    }

    @Test
    void handleAllExceptions_GcpError() throws Exception {
        ChatRequest request = new ChatRequest("Test question");
        // Using a real GCP exception instead of a mock because getClass() is final
        Exception gcpEx = new com.google.cloud.storage.StorageException(500, "GCP Storage failure");
        
        when(geminiApiService.askQuestion(any())).thenThrow(gcpEx);

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("GCP_SERVICE_ERROR"))
                .andExpect(jsonPath("$.message").value("A background Google Cloud service encountered an issue."));
    }

    @Test
    void handleAllExceptions_NullMessage() throws Exception {
        ChatRequest request = new ChatRequest("Test question");
        when(geminiApiService.askQuestion(any())).thenThrow(new RuntimeException((String)null));

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.details.technicalMessage").value("No details available"));
    }
}
