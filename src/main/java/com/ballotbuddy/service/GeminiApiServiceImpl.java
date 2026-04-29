package com.ballotbuddy.service;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link GeminiApiService} handling external AI and local fallbacks.
 */
@Slf4j
@Service
public class GeminiApiServiceImpl implements GeminiApiService {

    private final RestTemplate restTemplate;
    private final ElectionService electionService;
    private final StateElectionRepository stateRepository;

    @Value("${google.gemini.api.key}")
    private String apiKey;

    @Value("${google.gemini.api.url}")
    private String apiUrl;

    public GeminiApiServiceImpl(ElectionService electionService, StateElectionRepository stateRepository) {
        this.restTemplate = new RestTemplate();
        this.electionService = electionService;
        this.stateRepository = stateRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatResponse askQuestion(ChatRequest request) {
        log.info("Processing user question: {}", request.getQuery());

        if (apiKey == null || apiKey.equals("REPLACE_ME") || apiKey.isEmpty()) {
            log.warn("Gemini API key not configured. Using local H2 fallback.");
            return getFallbackResponse(request.getQuery());
        }

        String context = electionService.getTimelineContext();
        String prompt = String.format(
            "You are Ballot Buddy, an elite election guide. Use the following context to answer the user's question accurately.\n\n" +
            "Context:\n%s\n\nUser Question: %s", context, request.getQuery()
        );

        String fullUrl = apiUrl + "?key=" + apiKey;
        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(fullUrl, entity, Map.class);
            String aiText = extractTextFromResponse(response);
            return buildResponse(aiText);
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}. Switching to H2 Fallback.", e.getMessage());
            return getFallbackResponse(request.getQuery());
        }
    }

    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}", e.getMessage());
            return "Error parsing response from AI.";
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
