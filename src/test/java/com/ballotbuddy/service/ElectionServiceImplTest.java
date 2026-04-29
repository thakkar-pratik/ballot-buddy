package com.ballotbuddy.service;

import com.ballotbuddy.dto.ElectionStepResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElectionServiceImplTest {

    @Mock
    private StateElectionRepository stateRepository;

    private ElectionService electionService;

    @BeforeEach
    void setUp() {
        electionService = new ElectionServiceImpl(stateRepository);
    }

    @Test
    void getTimeline_ShouldReturnAllSteps() {
        List<ElectionStepResponse> timeline = electionService.getTimeline();
        assertNotNull(timeline);
        assertEquals(4, timeline.size());
    }

    @Test
    void getAllStates_ShouldReturnFromRepo() {
        when(stateRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(electionService.getAllStates().isEmpty());
    }

    @Test
    void getTimelineContext_WithStates_ShouldIncludeStateData() {
        StateElection state = StateElection.builder()
                .stateName("Bihar")
                .voterCount(70000000L)
                .parties("JDU, RJD")
                .mainParticipants("Nitish Kumar")
                .electionDate("2025")
                .build();
        when(stateRepository.findAll()).thenReturn(List.of(state));
        
        String context = electionService.getTimelineContext();
        assertTrue(context.contains("Bihar"));
        assertTrue(context.contains("70000000"));
    }
}
