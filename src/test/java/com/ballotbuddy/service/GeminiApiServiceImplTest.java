package com.ballotbuddy.service;

import com.ballotbuddy.dto.ChatRequest;
import com.ballotbuddy.dto.ChatResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiApiServiceImplTest {

    @Mock
    private ElectionService electionService;

    @Mock
    private StateElectionRepository stateRepository;

    @Mock
    private VertexAI vertexAI;

    @InjectMocks
    private GeminiApiServiceImpl geminiApiService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geminiApiService, "modelName", "gemini-1.5-pro");
    }

    @Test
    void askQuestion_Success() {
        ChatRequest request = new ChatRequest("When is election day?");
        when(electionService.getTimelineContext()).thenReturn("Context info");

        try (MockedConstruction<GenerativeModel> mockedModel = mockConstruction(GenerativeModel.class,
                (mock, context) -> {
                    GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
                    when(mock.generateContent(anyString())).thenReturn(mockResponse);
                });
             MockedStatic<ResponseHandler> mockedResponseHandler = mockStatic(ResponseHandler.class)) {
            
            mockedResponseHandler.when(() -> ResponseHandler.getText(any(GenerateContentResponse.class)))
                    .thenReturn("It is on Nov 5.");

            ChatResponse response = geminiApiService.askQuestion(request);

            assertNotNull(response);
            assertEquals("It is on Nov 5.", response.getResponse());
            assertEquals(1, mockedModel.constructed().size());
        }
    }

    @Test
    void askQuestion_SdkFailure_ReturnsFallback() {
        ChatRequest request = new ChatRequest("Maharashtra voters");
        // Triggering exception before or during SDK call
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("SDK Error"));
        
        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder()
                .stateName("Maharashtra")
                .voterCount(96000000L)
                .parties("BJP, NCP, SHS")
                .mainParticipants("Multiple Leaders")
                .electionDate("2024")
                .build()
        ));

        ChatResponse response = geminiApiService.askQuestion(request);

        assertNotNull(response);
        assertTrue(response.getResponse().contains("Local Intelligence"));
        assertTrue(response.getResponse().contains("Maharashtra"));
    }

    @Test
    void askQuestion_VoterKeywordFallback() {
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("Fail"));
        when(stateRepository.findAll()).thenReturn(List.of());

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("how to register as a voter"));
        assertTrue(response.getResponse().contains("NVSP portal"));
    }

    @Test
    void askQuestion_DefaultFallback() {
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("Fail"));
        when(stateRepository.findAll()).thenReturn(List.of());

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("random info"));
        assertTrue(response.getResponse().contains("Maharashtra, UP, Delhi, or Karnataka"));
    }

    @Test
    void askQuestion_StateNameMatchingFallback() {
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("Fail"));
        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder()
                .stateName("Delhi")
                .voterCount(15000000L)
                .parties("AAP, BJP, INC")
                .mainParticipants("Kejriwal")
                .electionDate("2025")
                .build()
        ));

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("tell me about Delhi"));
        assertTrue(response.getResponse().contains("Delhi"));
        assertTrue(response.getResponse().contains("15,000,000"));
        assertTrue(response.getResponse().contains("Local Intelligence"));
    }

    @Test
    void askQuestion_LoopNoMatch_VoterKeyword() {
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("Fail"));
        // First state doesn't match, loop continues
        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").build()
        ));

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("register me"));
        assertTrue(response.getResponse().contains("NVSP portal"));
    }

    @Test
    void askQuestion_MultiStateLoop_MatchesSecond() {
        when(electionService.getTimelineContext()).thenThrow(new RuntimeException("Fail"));
        when(stateRepository.findAll()).thenReturn(List.of(
            StateElection.builder().stateName("Maharashtra").voterCount(100L).build(),
            StateElection.builder().stateName("Delhi").voterCount(200L).parties("X").mainParticipants("Y").electionDate("Z").build()
        ));

        ChatResponse response = geminiApiService.askQuestion(new ChatRequest("info on Delhi"));
        assertTrue(response.getResponse().contains("Delhi"));
    }
}
