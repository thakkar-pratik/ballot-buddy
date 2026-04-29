package com.ballotbuddy.controller;

import com.ballotbuddy.dto.AnalyticsSnapshotDto;
import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.service.AnalyticsStorageService;
import com.ballotbuddy.service.GeminiApiService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final GeminiApiService geminiApiService;
    private final AnalyticsStorageService analyticsStorageService;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = geminiApiService.askQuestion(request);
        
        // Log analytics asynchronously or directly
        analyticsStorageService.saveSnapshot(AnalyticsSnapshotDto.builder()
                .sessionId(UUID.randomUUID().toString())
                .action("USER_QUERY")
                .timestamp(LocalDateTime.now())
                .metadata(request.getQuery())
                .build());

        return ResponseEntity.ok(response);
    }
}
