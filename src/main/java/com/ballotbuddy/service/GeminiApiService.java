package com.ballotbuddy.service;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;

/**
 * Interface for AI interaction services via Gemini.
 */
public interface GeminiApiService {
    /**
     * Processes a user query and returns an AI-generated or fallback response.
     * @param request The user's chat request.
     * @return The AI's response DTO.
     */
    ChatResponse askQuestion(ChatRequest request);
}
