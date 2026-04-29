package com.ballotbuddy.service;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeminiApiServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ElectionService electionService;

    @Mock
    private StateElectionRepository stateRepository;

    @InjectMocks
    private GeminiApiServiceImpl geminiApiService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geminiApiService, "apiKey", "test-key");
        ReflectionTestUtils.setField(geminiApiService, "apiUrl", "http://test-url");
        ReflectionTestUtils.setField(geminiApiService, "restTemplate", restTemplate);
    }

    @Test
    void askQuestion_Success() {
        ChatRequest request = new ChatRequest("When is election day?");
        when(electionService.getTimelineContext()).thenReturn("Context info");
        
        Map<String, Object> mockResponse = Map.of(
            "candidates", List.of(
                Map.of("content", Map.of(
                    "parts", List.of(Map.of("text", "It is on Nov 5."))
                ))
            )
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        ChatResponse response = geminiApiService.askQuestion(request);

        assertNotNull(response);
        assertEquals("It is on Nov 5.", response.getResponse());
    }

    @Test
    void askQuestion_ApiFailure_ReturnsFallback() {
        ChatRequest request = new ChatRequest("Maharashtra voters");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("API Down"));
        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").voterCount(96000000L).parties("BJP").mainParticipants("Shinde").electionDate("2024").build()
        ));

        ChatResponse response = geminiApiService.askQuestion(request);

        assertNotNull(response);
        assertTrue(response.getResponse().contains("Local Intelligence"));
        assertTrue(response.getResponse().contains("Maharashtra"));
    }

    @Test
    void askQuestion_VoterKeywordFallback() {
        ReflectionTestUtils.setField(geminiApiService, "apiKey", "REPLACE_ME");
        when(stateRepository.findAll()).thenReturn(Collections.emptyList());

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("how to register as a voter"));
        assertTrue(response.getResponse().contains("NVSP portal"));
    }

    @Test
    void askQuestion_DefaultFallback() {
        ReflectionTestUtils.setField(geminiApiService, "apiKey", "REPLACE_ME");
        when(stateRepository.findAll()).thenReturn(Collections.emptyList());

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("something random"));
        assertTrue(response.getResponse().contains("Maharashtra, UP, Delhi, or Karnataka"));
    }

    @Test
    void askQuestion_NullAndEmptyKeyBranches() {
        // Test Null Branch
        ReflectionTestUtils.setField(geminiApiService, "apiKey", null);
        assertNotNull(geminiApiService.askQuestion(new ChatRequest("test")));

        // Test Empty Branch
        ReflectionTestUtils.setField(geminiApiService, "apiKey", "");
        assertNotNull(geminiApiService.askQuestion(new ChatRequest("test")));
    }

    @Test
    void askQuestion_ParsingErrorBranch() {
        ChatRequest request = new ChatRequest("Help");
        when(electionService.getTimelineContext()).thenReturn("Context info");
        
        // Path 1: Empty map
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Collections.emptyMap());
        assertEquals("Error parsing response from AI.", geminiApiService.askQuestion(request).getResponse());

        // Path 2: Missing content
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Map.of("candidates", List.of(Collections.emptyMap())));
        assertEquals("Error parsing response from AI.", geminiApiService.askQuestion(request).getResponse());
    }

    @Test
    void askQuestion_ValidKeyBranch() {
        // Force all 3 conditions in 'if (apiKey == null || apiKey.equals("REPLACE_ME") || apiKey.isEmpty())' to be FALSE
        ReflectionTestUtils.setField(geminiApiService, "apiKey", "ACTUAL_KEY");
        when(electionService.getTimelineContext()).thenReturn("Context");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("API Error"));

        // This will pass the IF and hit the catch block for the API call
        assertNotNull(geminiApiService.askQuestion(new ChatRequest("test")));
    }

    @Test
    void askQuestion_ForLoopCompletesWithoutMatch_VoterKeyword() {
        // This tests the scenario where:
        // 1. API key is invalid (REPLACE_ME)
        // 2. Repository has states
        // 3. Query doesn't match any state name (loop completes without finding match)
        // 4. Query contains "voter" keyword -> goes to voter fallback

        ReflectionTestUtils.setField(geminiApiService, "apiKey", "REPLACE_ME");

        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").voterCount(96000000L).build(),
            StateElection.builder().stateName("Delhi").voterCount(15000000L).build()
        ));

        // Query contains "voter" but doesn't match any state name
        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("how to register as a voter"));

        assertNotNull(response);
        assertTrue(response.getResponse().contains("NVSP portal"));
        assertTrue(response.getResponse().contains("Local Intelligence"));
    }

    @Test
    void askQuestion_RegisterKeyword_FallsBackToVoterInfo() {
        // This tests the second branch of the OR condition:
        // Query contains "register" but NOT "voter"

        ReflectionTestUtils.setField(geminiApiService, "apiKey", "REPLACE_ME");

        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").voterCount(96000000L).build(),
            StateElection.builder().stateName("Delhi").voterCount(15000000L).build()
        ));

        // Query contains only "register" keyword (not "voter")
        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("how do I register for elections"));

        assertNotNull(response);
        assertTrue(response.getResponse().contains("NVSP portal"));
        assertTrue(response.getResponse().contains("Local Intelligence"));
    }

    @Test
    void askQuestion_ForLoopCompletesWithoutMatch_DefaultFallback() {
        // This tests the scenario where:
        // 1. API key is invalid (REPLACE_ME)
        // 2. Repository has states
        // 3. Query doesn't match any state name (loop completes without finding match)
        // 4. Query has no special keywords -> goes to default fallback

        ReflectionTestUtils.setField(geminiApiService, "apiKey", "REPLACE_ME");

        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").voterCount(96000000L).build(),
            StateElection.builder().stateName("Delhi").voterCount(15000000L).build()
        ));

        // Query that doesn't match any state or special keyword
        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("tell me about politics"));

        assertNotNull(response);
        assertTrue(response.getResponse().contains("Maharashtra, UP, Delhi, or Karnataka"));
        assertTrue(response.getResponse().contains("Local Intelligence"));
    }
}
