package com.ballotbuddy.service;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link GeminiApiService} handling external AI via Vertex AI SDK and local fallbacks.
 */
@Slf4j
@Service
public class GeminiApiServiceImpl implements GeminiApiService {

    private final ElectionService electionService;
    private final StateElectionRepository stateRepository;
    private final VertexAI vertexAI;

    @Value("${google.gemini.model:gemini-1.5-pro}")
    private String modelName;

    public GeminiApiServiceImpl(ElectionService electionService, StateElectionRepository stateRepository, VertexAI vertexAI) {
        this.electionService = electionService;
        this.stateRepository = stateRepository;
        this.vertexAI = vertexAI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatResponse askQuestion(ChatRequest request) {
        log.info("Processing user question via Vertex AI: {}", request.getQuery());

        try {
            String context = electionService.getTimelineContext();
            String prompt = String.format(
                "You are Ballot Buddy, an elite election guide. Use the following context to answer the user's question accurately.\n\n" +
                "Context:\n%s\n\nUser Question: %s", context, request.getQuery()
            );

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(prompt);
            String aiText = ResponseHandler.getText(response);
            
            return buildResponse(aiText);
        } catch (Exception e) {
            log.error("Vertex AI SDK error: {}. Switching to H2 Fallback.", e.getMessage());
            return getFallbackResponse(request.getQuery());
        }
    }

    private ChatResponse getFallbackResponse(String query) {
        String lowerQuery = query.toLowerCase();
        List<StateElection> allStates = stateRepository.findAll();
        
        for (StateElection state : allStates) {
            if (lowerQuery.contains(state.getStateName().toLowerCase())) {
                String response = String.format(
                    "According to my local records for %s, there are approximately %,d registered voters. " +
                    "The major parties participating are %s, and key leaders include %s. " +
                    "The next election is scheduled for %s. [Mode: Local Intelligence]",
                    state.getStateName(), state.getVoterCount(), 
                    state.getParties(), state.getMainParticipants(), state.getElectionDate()
                );
                return buildResponse(response);
            }
        }

        if (lowerQuery.contains("voter") || lowerQuery.contains("register")) {
            return buildResponse("To register as a voter in India, you must be 18+ and use the NVSP portal or Voter Helpline App. " +
                                 "I have specific data for Maharashtra, UP, Delhi, and Karnataka—feel free to ask about them! [Mode: Local Intelligence]");
        }

        return buildResponse("I am currently in Local Intelligence mode. I can provide detailed election statistics and participant information for Maharashtra, UP, Delhi, or Karnataka. [Mode: Local Intelligence]");
    }

    private ChatResponse buildResponse(String text) {
        return ChatResponse.builder()
                .response(text)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
