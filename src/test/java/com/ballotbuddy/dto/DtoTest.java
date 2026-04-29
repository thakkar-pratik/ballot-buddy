package com.ballotbuddy.dto;

import com.ballotbuddy.entity.StateElection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void chatRequest_AllMethods() {
        ChatRequest dto = new ChatRequest();
        dto.setQuery("test query");
        assertEquals("test query", dto.getQuery());
        
        ChatRequest dto2 = new ChatRequest("another query");
        assertEquals("another query", dto2.getQuery());
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertNotEquals(dto, null);
        assertNotEquals(dto, "string");
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void chatResponse_AllMethods() {
        ChatResponse dto = ChatResponse.builder()
                .response("AI response")
                .timestamp("2024-01-01")
                .build();
        
        assertEquals("AI response", dto.getResponse());
        assertEquals("2024-01-01", dto.getTimestamp());
        
        ChatResponse dto2 = new ChatResponse();
        dto2.setResponse("test");
        dto2.setTimestamp("now");
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void electionStepResponse_AllMethods() {
        ElectionStepResponse dto = ElectionStepResponse.builder()
                .id("1")
                .title("Title")
                .date("2024-01-01")
                .description("Description")
                .completed(true)
                .build();
        
        assertEquals("1", dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("2024-01-01", dto.getDate());
        assertEquals("Description", dto.getDescription());
        assertTrue(dto.isCompleted());
        
        ElectionStepResponse dto2 = new ElectionStepResponse();
        dto2.setId("2");
        dto2.setTitle("Title2");
        dto2.setDate("2024-02-01");
        dto2.setDescription("Desc2");
        dto2.setCompleted(false);
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void analyticsSnapshotDto_AllMethods() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder()
                .sessionId("session-1")
                .action("ACTION")
                .timestamp(now)
                .metadata("metadata")
                .build();
        
        assertEquals("session-1", dto.getSessionId());
        assertEquals("ACTION", dto.getAction());
        assertEquals(now, dto.getTimestamp());
        assertEquals("metadata", dto.getMetadata());
        
        AnalyticsSnapshotDto dto2 = new AnalyticsSnapshotDto();
        dto2.setSessionId("session-2");
        dto2.setAction("ACTION2");
        dto2.setTimestamp(now);
        dto2.setMetadata("meta2");
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void healthResponse_AllMethods() {
        HealthResponse dto = HealthResponse.builder()
                .status("UP")
                .message("Ready")
                .version("1.0.0")
                .build();
        
        assertEquals("UP", dto.getStatus());
        assertEquals("Ready", dto.getMessage());
        assertEquals("1.0.0", dto.getVersion());
        
        HealthResponse dto2 = new HealthResponse();
        dto2.setStatus("DOWN");
        dto2.setMessage("Not ready");
        dto2.setVersion("2.0.0");
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void errorResponse_AllMethods() {
        Map<String, String> details = Map.of("key", "value");
        ErrorResponse dto = ErrorResponse.builder()
                .errorCode("ERR001")
                .message("Error message")
                .details(details)
                .build();
        
        assertEquals("ERR001", dto.getErrorCode());
        assertEquals("Error message", dto.getMessage());
        assertEquals(details, dto.getDetails());
        
        ErrorResponse dto2 = new ErrorResponse();
        dto2.setErrorCode("ERR002");
        dto2.setMessage("Another error");
        dto2.setDetails(Map.of("key2", "value2"));
        
        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void timelineResponse_AllMethods() {
        TimelineResponse dto = TimelineResponse.builder()
                .steps(Collections.emptyList())
                .totalSteps(0)
                .build();

        assertNotNull(dto.getSteps());
        assertEquals(0, dto.getTotalSteps());

        TimelineResponse dto2 = new TimelineResponse();
        dto2.setSteps(Collections.singletonList(
            ElectionStepResponse.builder().id("1").build()
        ));
        dto2.setTotalSteps(1);

        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }

    @Test
    void stateListResponse_AllMethods() {
        StateListResponse dto = StateListResponse.builder()
                .states(Collections.emptyList())
                .count(0)
                .build();

        assertNotNull(dto.getStates());
        assertEquals(0, dto.getCount());

        StateListResponse dto2 = new StateListResponse();
        dto2.setStates(Collections.singletonList(
            StateElection.builder().stateName("Test").build()
        ));
        dto2.setCount(1);

        assertNotNull(dto.toString());
        assertEquals(dto, dto);
        assertNotEquals(dto, dto2);
        assertTrue(dto.hashCode() != 0);
    }
}
