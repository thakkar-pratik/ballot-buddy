package com.ballotbuddy.controller;

import com.ballotbuddy.dto.ElectionStepResponse;
import com.ballotbuddy.dto.StateListResponse;
import com.ballotbuddy.dto.TimelineResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.service.ElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller handling election timeline and state intelligence endpoints.
 */
@RestController
@RequestMapping("/api/election")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ElectionController {

    private final ElectionService electionService;

    /**
     * Endpoint to fetch the general election timeline.
     * @return TimelineResponse containing steps.
     */
    @GetMapping("/timeline")
    public ResponseEntity<TimelineResponse> getTimeline() {
        List<ElectionStepResponse> steps = electionService.getTimeline();
        return ResponseEntity.ok(TimelineResponse.builder()
                .steps(steps)
                .totalSteps(steps.size())
                .build());
    }

    /**
     * Endpoint to fetch all state-wise election intelligence.
     * @return StateListResponse containing states.
     */
    @GetMapping("/states")
    public ResponseEntity<StateListResponse> getStates() {
        List<StateElection> states = electionService.getAllStates();
        return ResponseEntity.ok(StateListResponse.builder()
                .states(states)
                .count(states.size())
                .build());
    }
}
