package com.ballotbuddy.controller;

import com.ballotbuddy.dto.ElectionStepResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.service.ElectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ElectionController.class)
class ElectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ElectionService electionService;

    @Test
    void getTimeline_ReturnsWrappedSteps() throws Exception {
        when(electionService.getTimeline()).thenReturn(List.of(
            ElectionStepResponse.builder().title("Step 1").build()
        ));

        mockMvc.perform(get("/api/election/timeline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.steps").isArray())
                .andExpect(jsonPath("$.totalSteps").value(1));
    }

    @Test
    void getStates_ReturnsWrappedStates() throws Exception {
        when(electionService.getAllStates()).thenReturn(List.of(
            StateElection.builder().stateName("Test State").build()
        ));

        mockMvc.perform(get("/api/election/states"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.states").isArray())
                .andExpect(jsonPath("$.count").value(1));
    }
}
