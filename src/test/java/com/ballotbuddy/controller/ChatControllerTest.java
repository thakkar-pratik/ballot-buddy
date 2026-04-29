package com.ballotbuddy.controller;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.service.AnalyticsStorageService;
import com.ballotbuddy.service.GeminiApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeminiApiService geminiApiService;

    @MockBean
    private AnalyticsStorageService analyticsStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void askQuestion_ValidRequest_ReturnsOk() throws Exception {
        ChatRequest request = new ChatRequest("Tell me about voting");
        ChatResponse response = ChatResponse.builder().response("Sure!").timestamp("now").build();

        when(geminiApiService.askQuestion(any(ChatRequest.class))).thenReturn(response);
        doNothing().when(analyticsStorageService).saveSnapshot(any());

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Sure!"));
    }

    @Test
    void askQuestion_InvalidRequest_ReturnsBadRequest() throws Exception {
        ChatRequest request = new ChatRequest(""); // Blank query

        mockMvc.perform(post("/api/chat/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
