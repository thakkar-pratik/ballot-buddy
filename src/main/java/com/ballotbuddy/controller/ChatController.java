package com.ballotbuddy.controller;

import com.ballotbuddy.dto.AnalyticsSnapshotDto;
import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.service.AnalyticsStorageService;
import com.ballotbuddy.service.CloudLoggingService;
import com.ballotbuddy.service.GeminiApiService;
import com.google.cloud.logging.Severity;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * REST Controller handling AI chat interactions.
 * Provides endpoints for users to ask election-related questions.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class ChatController {

    private static final String ACTION_USER_QUERY = "USER_QUERY";

    private final GeminiApiService geminiApiService;
    private final AnalyticsStorageService analyticsStorageService;
    private final CloudLoggingService cloudLoggingService;

    /**
     * Processes a user's chat request and returns an AI-generated response.
     * Logs the request and response asynchronously.
     *
     * @param request the validated chat request payload
     * @return a ResponseEntity containing the chat response
     */
    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@Valid @RequestBody ChatRequest request) {
        // Structured logging to GCP
        cloudLoggingService.log("Chat request received: " + request.getQuery(), Severity.INFO);

        ChatResponse response = geminiApiService.askQuestion(request);
        
        // Log analytics asynchronously to GCP Storage
        analyticsStorageService.saveSnapshot(AnalyticsSnapshotDto.builder()
                .sessionId(UUID.randomUUID().toString())
                .action(ACTION_USER_QUERY)
                .timestamp(LocalDateTime.now())
                .metadata(request.getQuery())
                .build());

        cloudLoggingService.log("Chat response dispatched successfully", Severity.INFO);
        return ResponseEntity.ok(response);
    }
}
