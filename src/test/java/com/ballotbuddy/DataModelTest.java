package com.ballotbuddy;

import com.ballotbuddy.dto.*;
import com.ballotbuddy.entity.StateElection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataModelTest {

    @Test
    void testDtoAndEntities() {
        // Test AnalyticsSnapshotDto
        AnalyticsSnapshotDto analytics = new AnalyticsSnapshotDto("sid", "act", LocalDateTime.now(), "meta");
        testLombok(analytics, new AnalyticsSnapshotDto());
        assertEquals("sid", analytics.getSessionId());

        // Test ChatRequest
        ChatRequest req = new ChatRequest("query");
        testLombok(req, new ChatRequest());
        assertEquals("query", req.getQuery());

        // Test ChatResponse
        ChatResponse res = ChatResponse.builder().response("r").timestamp("t").build();
        testLombok(res, new ChatResponse());
        assertEquals("r", res.getResponse());

        // Test ElectionStepResponse
        ElectionStepResponse step = ElectionStepResponse.builder().id("1").title("T").date("D").description("Desc").completed(true).build();
        testLombok(step, new ElectionStepResponse());
        assertEquals("1", step.getId());

        // Test ErrorResponse
        ErrorResponse err = new ErrorResponse("CODE", "MSG", Map.of("key", "val"));
        testLombok(err, new ErrorResponse());
        assertEquals("CODE", err.getErrorCode());

        // Test HealthResponse
        HealthResponse health = new HealthResponse("UP", "MSG", "V1");
        testLombok(health, new HealthResponse());
        assertEquals("UP", health.getStatus());

        // Test StateListResponse
        StateListResponse stateList = new StateListResponse(Collections.emptyList(), 0);
        testLombok(stateList, new StateListResponse());
        assertEquals(0, stateList.getCount());

        // Test TimelineResponse
        TimelineResponse timeline = new TimelineResponse(Collections.emptyList(), 0);
        testLombok(timeline, new TimelineResponse());
        assertEquals(0, timeline.getTotalSteps());

        // Test StateElection Entity
        StateElection se = StateElection.builder()
                .id(1L).stateName("S").voterCount(100L).parties("P").mainParticipants("M").electionDate("D").currentStatus("ST")
                .build();
        testLombok(se, new StateElection());
        assertEquals(1L, se.getId());
    }

    private void testLombok(Object obj, Object empty) {
        assertNotNull(obj.toString());
        assertNotNull(obj.hashCode());
        assertNotEquals(obj, empty);
        assertNotEquals(obj, null);
        assertEquals(obj, obj);
    }
}
