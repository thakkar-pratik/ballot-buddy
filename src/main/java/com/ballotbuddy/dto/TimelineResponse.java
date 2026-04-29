package com.ballotbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Wrapper DTO for the election timeline to avoid returning raw collections.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineResponse {
    private List<ElectionStepResponse> steps;
    private int totalSteps;
}
