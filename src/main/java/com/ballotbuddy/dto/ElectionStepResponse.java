package com.ballotbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a single step in the election timeline.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectionStepResponse {
    private String id;
    private String title;
    private String date;
    private String description;
    private boolean completed;
}
