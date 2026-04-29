package com.ballotbuddy.dto;

import com.ballotbuddy.entity.StateElection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Wrapper DTO for state-wise election data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateListResponse {
    private List<StateElection> states;
    private int count;
}
