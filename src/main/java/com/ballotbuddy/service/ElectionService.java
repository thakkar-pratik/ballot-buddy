package com.ballotbuddy.service;

import com.ballotbuddy.dto.ElectionStepResponse;
import com.ballotbuddy.entity.StateElection;
import java.util.List;

/**
 * Interface for Election Timeline and State Information services.
 */
public interface ElectionService {
    /**
     * Retrieves the general election timeline steps.
     * @return List of election steps.
     */
    List<ElectionStepResponse> getTimeline();

    /**
     * Retrieves all state-wise election data.
     * @return List of state election entities.
     */
    List<StateElection> getAllStates();

    /**
     * Generates a text context of the timeline and states for AI processing.
     * @return Formatted context string.
     */
    String getTimelineContext();
}
