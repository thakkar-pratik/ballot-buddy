package com.ballotbuddy.dto;

import com.ballotbuddy.entity.StateElection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exhaustive unit tests for all ballot-buddy DTOs.
 * Targets 100 % line + branch coverage on Lombok-generated
 * and hand-written code for:
 *   ChatRequest, ChatResponse, ElectionStepResponse,
 *   AnalyticsSnapshotDto, HealthResponse, ErrorResponse,
 *   TimelineResponse, StateListResponse
 */
class DtoTest {

    // ──────────────────────────────────────────────
    // ChatRequest
    // ──────────────────────────────────────────────
    @Test
    void chatRequest_noArgConstructor() {
        ChatRequest dto = new ChatRequest();
        assertNull(dto.getQuery());
    }

    @Test
    void chatRequest_allArgConstructor() {
        ChatRequest dto = new ChatRequest("election info");
        assertEquals("election info", dto.getQuery());
    }

    @Test
    void chatRequest_setterGetter() {
        ChatRequest dto = new ChatRequest();
        dto.setQuery("who can vote");
        assertEquals("who can vote", dto.getQuery());
    }

    @Test
    void chatRequest_toString() {
        ChatRequest dto = new ChatRequest("q");
        String s = dto.toString();
        assertNotNull(s);
        assertTrue(s.contains("q"));
    }

    @Test
    void chatRequest_equalsReflexive() {
        ChatRequest dto = new ChatRequest("q");
        assertEquals(dto, dto);
    }

    @Test
    void chatRequest_equalsSymmetric() {
        ChatRequest a = new ChatRequest("q");
        ChatRequest b = new ChatRequest("q");
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void chatRequest_equalsNull() {
        ChatRequest dto = new ChatRequest("q");
        assertNotEquals(dto, null);
    }

    @Test
    void chatRequest_equalsDifferentType() {
        ChatRequest dto = new ChatRequest("q");
        assertNotEquals(dto, "string");
    }

    @Test
    void chatRequest_notEqualsDifferentValue() {
        ChatRequest a = new ChatRequest("q1");
        ChatRequest b = new ChatRequest("q2");
        assertNotEquals(a, b);
    }

    @Test
    void chatRequest_hashCodeConsistent() {
        ChatRequest a = new ChatRequest("q");
        ChatRequest b = new ChatRequest("q");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void chatRequest_hashCodeNullField() {
        ChatRequest dto = new ChatRequest();
        // should not throw
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void chatRequest_canEqual() {
        ChatRequest a = new ChatRequest("q");
        assertTrue(a.canEqual(new ChatRequest()));
        assertFalse(a.canEqual("string"));
    }

    // ──────────────────────────────────────────────
    // ChatResponse
    // ──────────────────────────────────────────────
    @Test
    void chatResponse_noArgConstructor() {
        ChatResponse dto = new ChatResponse();
        assertNull(dto.getResponse());
        assertNull(dto.getTimestamp());
    }

    @Test
    void chatResponse_allArgConstructor() {
        ChatResponse dto = new ChatResponse("AI says hello", "2024-01-01");
        assertEquals("AI says hello", dto.getResponse());
        assertEquals("2024-01-01", dto.getTimestamp());
    }

    @Test
    void chatResponse_builder() {
        ChatResponse dto = ChatResponse.builder()
                .response("resp")
                .timestamp("ts")
                .build();
        assertEquals("resp", dto.getResponse());
        assertEquals("ts", dto.getTimestamp());
    }

    @Test
    void chatResponse_setters() {
        ChatResponse dto = new ChatResponse();
        dto.setResponse("r");
        dto.setTimestamp("t");
        assertEquals("r", dto.getResponse());
        assertEquals("t", dto.getTimestamp());
    }

    @Test
    void chatResponse_equalsAndHashCode() {
        ChatResponse a = ChatResponse.builder().response("r").timestamp("t").build();
        ChatResponse b = ChatResponse.builder().response("r").timestamp("t").build();
        ChatResponse c = ChatResponse.builder().response("x").timestamp("t").build();

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void chatResponse_canEqual() {
        ChatResponse a = new ChatResponse();
        assertTrue(a.canEqual(new ChatResponse()));
        assertFalse(a.canEqual("x"));
    }

    @Test
    void chatResponse_toString() {
        ChatResponse dto = ChatResponse.builder().response("r").timestamp("t").build();
        String s = dto.toString();
        assertTrue(s.contains("r"));
        assertTrue(s.contains("t"));
    }

    @Test
    void chatResponse_hashCodeNullFields() {
        ChatResponse dto = new ChatResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void chatResponse_equalsDifferentTimestamp() {
        ChatResponse a = ChatResponse.builder().response("r").timestamp("t1").build();
        ChatResponse b = ChatResponse.builder().response("r").timestamp("t2").build();
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // ElectionStepResponse
    // ──────────────────────────────────────────────
    @Test
    void electionStep_noArgConstructor() {
        ElectionStepResponse dto = new ElectionStepResponse();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDate());
        assertNull(dto.getDescription());
        assertFalse(dto.isCompleted());
    }

    @Test
    void electionStep_allArgConstructor() {
        ElectionStepResponse dto = new ElectionStepResponse("1", "Title", "2024-01-01", "Desc", true);
        assertEquals("1", dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("2024-01-01", dto.getDate());
        assertEquals("Desc", dto.getDescription());
        assertTrue(dto.isCompleted());
    }

    @Test
    void electionStep_builder() {
        ElectionStepResponse dto = ElectionStepResponse.builder()
                .id("2")
                .title("T")
                .date("D")
                .description("Desc")
                .completed(false)
                .build();
        assertEquals("2", dto.getId());
        assertFalse(dto.isCompleted());
    }

    @Test
    void electionStep_setters() {
        ElectionStepResponse dto = new ElectionStepResponse();
        dto.setId("id");
        dto.setTitle("title");
        dto.setDate("date");
        dto.setDescription("desc");
        dto.setCompleted(true);
        assertEquals("id", dto.getId());
        assertEquals("title", dto.getTitle());
        assertEquals("date", dto.getDate());
        assertEquals("desc", dto.getDescription());
        assertTrue(dto.isCompleted());
    }

    @Test
    void electionStep_equalsAndHashCode() {
        ElectionStepResponse a = ElectionStepResponse.builder().id("1").title("T").date("D").description("Desc").completed(true).build();
        ElectionStepResponse b = ElectionStepResponse.builder().id("1").title("T").date("D").description("Desc").completed(true).build();
        ElectionStepResponse c = ElectionStepResponse.builder().id("2").title("T").date("D").description("Desc").completed(true).build();

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, 42);
    }

    @Test
    void electionStep_canEqual() {
        ElectionStepResponse a = new ElectionStepResponse();
        assertTrue(a.canEqual(new ElectionStepResponse()));
        assertFalse(a.canEqual(new Object()));
    }

    @Test
    void electionStep_toString() {
        ElectionStepResponse dto = ElectionStepResponse.builder().id("1").build();
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("1"));
    }

    @Test
    void electionStep_hashCodeNullFields() {
        ElectionStepResponse dto = new ElectionStepResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void electionStep_completedBranchCoverage() {
        ElectionStepResponse a = ElectionStepResponse.builder().completed(true).build();
        ElectionStepResponse b = ElectionStepResponse.builder().completed(false).build();
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // AnalyticsSnapshotDto
    // ──────────────────────────────────────────────
    @Test
    void analytics_noArgConstructor() {
        AnalyticsSnapshotDto dto = new AnalyticsSnapshotDto();
        assertNull(dto.getSessionId());
        assertNull(dto.getAction());
        assertNull(dto.getTimestamp());
        assertNull(dto.getMetadata());
    }

    @Test
    void analytics_allArgConstructor() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        AnalyticsSnapshotDto dto = new AnalyticsSnapshotDto("s1", "CLICK", now, "meta");
        assertEquals("s1", dto.getSessionId());
        assertEquals("CLICK", dto.getAction());
        assertEquals(now, dto.getTimestamp());
        assertEquals("meta", dto.getMetadata());
    }

    @Test
    void analytics_builder() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder()
                .sessionId("sess")
                .action("VIEW")
                .timestamp(now)
                .metadata("m")
                .build();
        assertEquals("sess", dto.getSessionId());
        assertEquals("VIEW", dto.getAction());
        assertEquals(now, dto.getTimestamp());
        assertEquals("m", dto.getMetadata());
    }

    @Test
    void analytics_setters() {
        AnalyticsSnapshotDto dto = new AnalyticsSnapshotDto();
        LocalDateTime ts = LocalDateTime.now();
        dto.setSessionId("s");
        dto.setAction("a");
        dto.setTimestamp(ts);
        dto.setMetadata("md");
        assertEquals("s", dto.getSessionId());
        assertEquals("a", dto.getAction());
        assertEquals(ts, dto.getTimestamp());
        assertEquals("md", dto.getMetadata());
    }

    @Test
    void analytics_equalsAndHashCode() {
        LocalDateTime now = LocalDateTime.of(2024, 6, 15, 10, 30);
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto("s", "a", now, "m");
        AnalyticsSnapshotDto b = new AnalyticsSnapshotDto("s", "a", now, "m");
        AnalyticsSnapshotDto c = new AnalyticsSnapshotDto("s2", "a", now, "m");

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "other");
    }

    @Test
    void analytics_canEqual() {
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto();
        assertTrue(a.canEqual(new AnalyticsSnapshotDto()));
        assertFalse(a.canEqual(123));
    }

    @Test
    void analytics_toString() {
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder().sessionId("s").build();
        String s = dto.toString();
        assertNotNull(s);
        assertTrue(s.contains("s"));
    }

    @Test
    void analytics_hashCodeNullFields() {
        AnalyticsSnapshotDto dto = new AnalyticsSnapshotDto();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void analytics_equalsDifferentAction() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto("s", "a1", now, "m");
        AnalyticsSnapshotDto b = new AnalyticsSnapshotDto("s", "a2", now, "m");
        assertNotEquals(a, b);
    }

    @Test
    void analytics_equalsDifferentTimestamp() {
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto("s", "a", LocalDateTime.of(2024, 1, 1, 0, 0), "m");
        AnalyticsSnapshotDto b = new AnalyticsSnapshotDto("s", "a", LocalDateTime.of(2025, 1, 1, 0, 0), "m");
        assertNotEquals(a, b);
    }

    @Test
    void analytics_equalsDifferentMetadata() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto("s", "a", now, "m1");
        AnalyticsSnapshotDto b = new AnalyticsSnapshotDto("s", "a", now, "m2");
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // HealthResponse
    // ──────────────────────────────────────────────
    @Test
    void health_noArgConstructor() {
        HealthResponse dto = new HealthResponse();
        assertNull(dto.getStatus());
        assertNull(dto.getMessage());
        assertNull(dto.getVersion());
    }

    @Test
    void health_allArgConstructor() {
        HealthResponse dto = new HealthResponse("UP", "Ready", "1.0.0");
        assertEquals("UP", dto.getStatus());
        assertEquals("Ready", dto.getMessage());
        assertEquals("1.0.0", dto.getVersion());
    }

    @Test
    void health_builder() {
        HealthResponse dto = HealthResponse.builder()
                .status("DOWN")
                .message("Offline")
                .version("2.0.0")
                .build();
        assertEquals("DOWN", dto.getStatus());
        assertEquals("Offline", dto.getMessage());
        assertEquals("2.0.0", dto.getVersion());
    }

    @Test
    void health_setters() {
        HealthResponse dto = new HealthResponse();
        dto.setStatus("UP");
        dto.setMessage("OK");
        dto.setVersion("1.0");
        assertEquals("UP", dto.getStatus());
        assertEquals("OK", dto.getMessage());
        assertEquals("1.0", dto.getVersion());
    }

    @Test
    void health_equalsAndHashCode() {
        HealthResponse a = new HealthResponse("UP", "OK", "1.0");
        HealthResponse b = new HealthResponse("UP", "OK", "1.0");
        HealthResponse c = new HealthResponse("DOWN", "OK", "1.0");

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    void health_canEqual() {
        HealthResponse a = new HealthResponse();
        assertTrue(a.canEqual(new HealthResponse()));
        assertFalse(a.canEqual("x"));
    }

    @Test
    void health_toString() {
        HealthResponse dto = HealthResponse.builder().status("UP").build();
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("UP"));
    }

    @Test
    void health_hashCodeNullFields() {
        HealthResponse dto = new HealthResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void health_equalsDifferentMessage() {
        HealthResponse a = new HealthResponse("UP", "OK", "1.0");
        HealthResponse b = new HealthResponse("UP", "NOT OK", "1.0");
        assertNotEquals(a, b);
    }

    @Test
    void health_equalsDifferentVersion() {
        HealthResponse a = new HealthResponse("UP", "OK", "1.0");
        HealthResponse b = new HealthResponse("UP", "OK", "2.0");
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // ErrorResponse
    // ──────────────────────────────────────────────
    @Test
    void error_noArgConstructor() {
        ErrorResponse dto = new ErrorResponse();
        assertNull(dto.getErrorCode());
        assertNull(dto.getMessage());
        assertNull(dto.getDetails());
    }

    @Test
    void error_allArgConstructor() {
        Map<String, String> details = Map.of("field", "invalid");
        ErrorResponse dto = new ErrorResponse("ERR001", "Validation error", details);
        assertEquals("ERR001", dto.getErrorCode());
        assertEquals("Validation error", dto.getMessage());
        assertEquals(details, dto.getDetails());
    }

    @Test
    void error_builder() {
        Map<String, String> details = Map.of("k", "v");
        ErrorResponse dto = ErrorResponse.builder()
                .errorCode("E1")
                .message("msg")
                .details(details)
                .build();
        assertEquals("E1", dto.getErrorCode());
        assertEquals("msg", dto.getMessage());
        assertEquals(details, dto.getDetails());
    }

    @Test
    void error_setters() {
        ErrorResponse dto = new ErrorResponse();
        Map<String, String> details = new HashMap<>();
        details.put("key", "value");
        dto.setErrorCode("E");
        dto.setMessage("M");
        dto.setDetails(details);
        assertEquals("E", dto.getErrorCode());
        assertEquals("M", dto.getMessage());
        assertEquals(details, dto.getDetails());
    }

    @Test
    void error_equalsAndHashCode() {
        Map<String, String> d = Map.of("k", "v");
        ErrorResponse a = new ErrorResponse("E1", "M", d);
        ErrorResponse b = new ErrorResponse("E1", "M", d);
        ErrorResponse c = new ErrorResponse("E2", "M", d);

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "x");
    }

    @Test
    void error_canEqual() {
        ErrorResponse a = new ErrorResponse();
        assertTrue(a.canEqual(new ErrorResponse()));
        assertFalse(a.canEqual(42));
    }

    @Test
    void error_toString() {
        ErrorResponse dto = ErrorResponse.builder().errorCode("E").build();
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("E"));
    }

    @Test
    void error_hashCodeNullFields() {
        ErrorResponse dto = new ErrorResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void error_equalsDifferentMessage() {
        Map<String, String> d = Map.of("k", "v");
        ErrorResponse a = new ErrorResponse("E", "M1", d);
        ErrorResponse b = new ErrorResponse("E", "M2", d);
        assertNotEquals(a, b);
    }

    @Test
    void error_equalsDifferentDetails() {
        ErrorResponse a = new ErrorResponse("E", "M", Map.of("k1", "v1"));
        ErrorResponse b = new ErrorResponse("E", "M", Map.of("k2", "v2"));
        assertNotEquals(a, b);
    }

    @Test
    void error_equalsNullDetails() {
        ErrorResponse a = new ErrorResponse("E", "M", null);
        ErrorResponse b = new ErrorResponse("E", "M", Map.of("k", "v"));
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void error_emptyDetails() {
        ErrorResponse dto = ErrorResponse.builder()
                .errorCode("E")
                .message("M")
                .details(Collections.emptyMap())
                .build();
        assertNotNull(dto.getDetails());
        assertTrue(dto.getDetails().isEmpty());
    }

    // ──────────────────────────────────────────────
    // TimelineResponse
    // ──────────────────────────────────────────────
    @Test
    void timeline_noArgConstructor() {
        TimelineResponse dto = new TimelineResponse();
        assertNull(dto.getSteps());
        assertEquals(0, dto.getTotalSteps());
    }

    @Test
    void timeline_allArgConstructor() {
        List<ElectionStepResponse> steps = List.of(
                ElectionStepResponse.builder().id("1").build()
        );
        TimelineResponse dto = new TimelineResponse(steps, 1);
        assertEquals(steps, dto.getSteps());
        assertEquals(1, dto.getTotalSteps());
    }

    @Test
    void timeline_builder() {
        TimelineResponse dto = TimelineResponse.builder()
                .steps(Collections.emptyList())
                .totalSteps(0)
                .build();
        assertNotNull(dto.getSteps());
        assertTrue(dto.getSteps().isEmpty());
        assertEquals(0, dto.getTotalSteps());
    }

    @Test
    void timeline_setters() {
        TimelineResponse dto = new TimelineResponse();
        List<ElectionStepResponse> steps = new ArrayList<>();
        steps.add(ElectionStepResponse.builder().id("s1").build());
        dto.setSteps(steps);
        dto.setTotalSteps(1);
        assertEquals(1, dto.getSteps().size());
        assertEquals(1, dto.getTotalSteps());
    }

    @Test
    void timeline_equalsAndHashCode() {
        List<ElectionStepResponse> steps = List.of(ElectionStepResponse.builder().id("1").build());
        TimelineResponse a = new TimelineResponse(steps, 1);
        TimelineResponse b = new TimelineResponse(steps, 1);
        TimelineResponse c = new TimelineResponse(steps, 2);

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "x");
    }

    @Test
    void timeline_canEqual() {
        TimelineResponse a = new TimelineResponse();
        assertTrue(a.canEqual(new TimelineResponse()));
        assertFalse(a.canEqual("x"));
    }

    @Test
    void timeline_toString() {
        TimelineResponse dto = TimelineResponse.builder().totalSteps(5).build();
        assertNotNull(dto.toString());
    }

    @Test
    void timeline_hashCodeNullFields() {
        TimelineResponse dto = new TimelineResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void timeline_equalsDifferentSteps() {
        TimelineResponse a = new TimelineResponse(List.of(ElectionStepResponse.builder().id("1").build()), 1);
        TimelineResponse b = new TimelineResponse(List.of(ElectionStepResponse.builder().id("2").build()), 1);
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // StateListResponse
    // ──────────────────────────────────────────────
    @Test
    void stateList_noArgConstructor() {
        StateListResponse dto = new StateListResponse();
        assertNull(dto.getStates());
        assertEquals(0, dto.getCount());
    }

    @Test
    void stateList_allArgConstructor() {
        List<StateElection> states = List.of(StateElection.builder().stateName("Maharashtra").build());
        StateListResponse dto = new StateListResponse(states, 1);
        assertEquals(states, dto.getStates());
        assertEquals(1, dto.getCount());
    }

    @Test
    void stateList_builder() {
        StateListResponse dto = StateListResponse.builder()
                .states(Collections.emptyList())
                .count(0)
                .build();
        assertNotNull(dto.getStates());
        assertEquals(0, dto.getCount());
    }

    @Test
    void stateList_setters() {
        StateListResponse dto = new StateListResponse();
        List<StateElection> states = new ArrayList<>();
        states.add(StateElection.builder().stateName("Karnataka").build());
        dto.setStates(states);
        dto.setCount(1);
        assertEquals(1, dto.getStates().size());
        assertEquals(1, dto.getCount());
    }

    @Test
    void stateList_equalsAndHashCode() {
        List<StateElection> states = List.of(StateElection.builder().stateName("Maharashtra").build());
        StateListResponse a = new StateListResponse(states, 1);
        StateListResponse b = new StateListResponse(states, 1);
        StateListResponse c = new StateListResponse(states, 2);

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "x");
    }

    @Test
    void stateList_canEqual() {
        StateListResponse a = new StateListResponse();
        assertTrue(a.canEqual(new StateListResponse()));
        assertFalse(a.canEqual("x"));
    }

    @Test
    void stateList_toString() {
        StateListResponse dto = StateListResponse.builder().count(3).build();
        assertNotNull(dto.toString());
    }

    @Test
    void stateList_hashCodeNullFields() {
        StateListResponse dto = new StateListResponse();
        int h = dto.hashCode();
        assertEquals(h, dto.hashCode());
    }

    @Test
    void stateList_equalsDifferentStates() {
        StateListResponse a = new StateListResponse(
                List.of(StateElection.builder().stateName("A").build()), 1);
        StateListResponse b = new StateListResponse(
                List.of(StateElection.builder().stateName("B").build()), 1);
        assertNotEquals(a, b);
    }

    @Test
    void stateList_nullVsNonNullStates() {
        StateListResponse a = new StateListResponse(null, 0);
        StateListResponse b = new StateListResponse(Collections.emptyList(), 0);
        assertNotEquals(a, b);
    }

    // ──────────────────────────────────────────────
    // Cross-DTO equals sanity
    // ──────────────────────────────────────────────
    @Test
    void crossDtoEquality_differentDtoTypesAreNeverEqual() {
        ChatRequest chatReq = new ChatRequest("q");
        ChatResponse chatResp = new ChatResponse("r", "t");
        HealthResponse health = new HealthResponse("UP", "OK", "1.0");
        ErrorResponse error = new ErrorResponse("E", "M", null);

        assertNotEquals(chatReq, chatResp);
        assertNotEquals(chatResp, health);
        assertNotEquals(health, error);
    }

    // ══════════════════════════════════════════════
    // Builder.toString() coverage
    // ══════════════════════════════════════════════
    @Test
    void chatResponseBuilder_toString() {
        String s = ChatResponse.builder().response("r").timestamp("t").toString();
        assertNotNull(s);
        assertTrue(s.contains("r"));
        assertTrue(s.contains("t"));
    }

    @Test
    void electionStepBuilder_toString() {
        String s = ElectionStepResponse.builder().id("1").title("T").date("D").description("Desc").completed(true).toString();
        assertNotNull(s);
        assertTrue(s.contains("1"));
    }

    @Test
    void analyticsBuilder_toString() {
        String s = AnalyticsSnapshotDto.builder().sessionId("s").action("a").metadata("m").toString();
        assertNotNull(s);
        assertTrue(s.contains("s"));
    }

    @Test
    void healthBuilder_toString() {
        String s = HealthResponse.builder().status("UP").message("OK").version("1.0").toString();
        assertNotNull(s);
        assertTrue(s.contains("UP"));
    }

    @Test
    void errorBuilder_toString() {
        String s = ErrorResponse.builder().errorCode("E").message("M").toString();
        assertNotNull(s);
        assertTrue(s.contains("E"));
    }

    @Test
    void timelineBuilder_toString() {
        String s = TimelineResponse.builder().totalSteps(5).toString();
        assertNotNull(s);
    }

    @Test
    void stateListBuilder_toString() {
        String s = StateListResponse.builder().count(3).toString();
        assertNotNull(s);
    }

    // ══════════════════════════════════════════════
    // Exhaustive equals branch coverage
    // (null vs non-null for EVERY field in each DTO)
    // ══════════════════════════════════════════════

    // --- ChatResponse ---
    @Test
    void chatResponse_equalsNullResponseVsNonNull() {
        ChatResponse a = ChatResponse.builder().response(null).timestamp("t").build();
        ChatResponse b = ChatResponse.builder().response("r").timestamp("t").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void chatResponse_equalsNullTimestampVsNonNull() {
        ChatResponse a = ChatResponse.builder().response("r").timestamp(null).build();
        ChatResponse b = ChatResponse.builder().response("r").timestamp("t").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void chatResponse_equalsBothNull() {
        ChatResponse a = new ChatResponse();
        ChatResponse b = new ChatResponse();
        assertEquals(a, b);
    }

    // --- ElectionStepResponse ---
    @Test
    void electionStep_equalsNullIdVsNonNull() {
        ElectionStepResponse a = ElectionStepResponse.builder().id(null).build();
        ElectionStepResponse b = ElectionStepResponse.builder().id("1").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void electionStep_equalsNullTitleVsNonNull() {
        ElectionStepResponse a = ElectionStepResponse.builder().id("1").title(null).build();
        ElectionStepResponse b = ElectionStepResponse.builder().id("1").title("T").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void electionStep_equalsNullDateVsNonNull() {
        ElectionStepResponse a = ElectionStepResponse.builder().id("1").title("T").date(null).build();
        ElectionStepResponse b = ElectionStepResponse.builder().id("1").title("T").date("D").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void electionStep_equalsNullDescriptionVsNonNull() {
        ElectionStepResponse a = ElectionStepResponse.builder().id("1").title("T").date("D").description(null).build();
        ElectionStepResponse b = ElectionStepResponse.builder().id("1").title("T").date("D").description("Desc").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void electionStep_equalsBothAllNull() {
        ElectionStepResponse a = new ElectionStepResponse();
        ElectionStepResponse b = new ElectionStepResponse();
        assertEquals(a, b);
    }

    // --- AnalyticsSnapshotDto ---
    @Test
    void analytics_equalsNullSessionIdVsNonNull() {
        AnalyticsSnapshotDto a = AnalyticsSnapshotDto.builder().sessionId(null).build();
        AnalyticsSnapshotDto b = AnalyticsSnapshotDto.builder().sessionId("s").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void analytics_equalsNullActionVsNonNull() {
        AnalyticsSnapshotDto a = AnalyticsSnapshotDto.builder().sessionId("s").action(null).build();
        AnalyticsSnapshotDto b = AnalyticsSnapshotDto.builder().sessionId("s").action("a").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void analytics_equalsNullTimestampVsNonNull() {
        AnalyticsSnapshotDto a = AnalyticsSnapshotDto.builder().sessionId("s").action("a").timestamp(null).build();
        AnalyticsSnapshotDto b = AnalyticsSnapshotDto.builder().sessionId("s").action("a").timestamp(LocalDateTime.now()).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void analytics_equalsNullMetadataVsNonNull() {
        LocalDateTime ts = LocalDateTime.now();
        AnalyticsSnapshotDto a = AnalyticsSnapshotDto.builder().sessionId("s").action("a").timestamp(ts).metadata(null).build();
        AnalyticsSnapshotDto b = AnalyticsSnapshotDto.builder().sessionId("s").action("a").timestamp(ts).metadata("m").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void analytics_equalsBothAllNull() {
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto();
        AnalyticsSnapshotDto b = new AnalyticsSnapshotDto();
        assertEquals(a, b);
    }

    // --- HealthResponse ---
    @Test
    void health_equalsNullStatusVsNonNull() {
        HealthResponse a = HealthResponse.builder().status(null).build();
        HealthResponse b = HealthResponse.builder().status("UP").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void health_equalsNullMessageVsNonNull() {
        HealthResponse a = HealthResponse.builder().status("UP").message(null).build();
        HealthResponse b = HealthResponse.builder().status("UP").message("OK").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void health_equalsNullVersionVsNonNull() {
        HealthResponse a = HealthResponse.builder().status("UP").message("OK").version(null).build();
        HealthResponse b = HealthResponse.builder().status("UP").message("OK").version("1.0").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void health_equalsBothAllNull() {
        HealthResponse a = new HealthResponse();
        HealthResponse b = new HealthResponse();
        assertEquals(a, b);
    }

    // --- ErrorResponse ---
    @Test
    void error_equalsNullErrorCodeVsNonNull() {
        ErrorResponse a = ErrorResponse.builder().errorCode(null).build();
        ErrorResponse b = ErrorResponse.builder().errorCode("E").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void error_equalsNullMessageVsNonNull() {
        ErrorResponse a = ErrorResponse.builder().errorCode("E").message(null).build();
        ErrorResponse b = ErrorResponse.builder().errorCode("E").message("M").build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void error_equalsBothAllNull() {
        ErrorResponse a = new ErrorResponse();
        ErrorResponse b = new ErrorResponse();
        assertEquals(a, b);
    }

    // --- TimelineResponse ---
    @Test
    void timeline_equalsNullStepsVsNonNull() {
        TimelineResponse a = TimelineResponse.builder().steps(null).totalSteps(0).build();
        TimelineResponse b = TimelineResponse.builder().steps(Collections.emptyList()).totalSteps(0).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void timeline_equalsDifferentTotalSteps() {
        TimelineResponse a = TimelineResponse.builder().totalSteps(1).build();
        TimelineResponse b = TimelineResponse.builder().totalSteps(2).build();
        assertNotEquals(a, b);
    }

    @Test
    void timeline_equalsBothAllNull() {
        TimelineResponse a = new TimelineResponse();
        TimelineResponse b = new TimelineResponse();
        assertEquals(a, b);
    }

    // --- StateListResponse ---
    @Test
    void stateList_equalsBothAllNull() {
        StateListResponse a = new StateListResponse();
        StateListResponse b = new StateListResponse();
        assertEquals(a, b);
    }

    @Test
    void stateList_equalsDifferentCount() {
        StateListResponse a = StateListResponse.builder().count(1).build();
        StateListResponse b = StateListResponse.builder().count(2).build();
        assertNotEquals(a, b);
    }

    // --- ChatRequest ---
    @Test
    void chatRequest_equalsNullQueryVsNonNull() {
        ChatRequest a = new ChatRequest();
        ChatRequest b = new ChatRequest("q");
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void chatRequest_equalsBothNull() {
        ChatRequest a = new ChatRequest();
        ChatRequest b = new ChatRequest();
        assertEquals(a, b);
    }

    // ══════════════════════════════════════════════
    // canEqual returning false (subclass) — hits the
    // "if (!other.canEqual(this)) return false" branch
    // in a.equals(sub): sub.canEqual(a) → false
    // ══════════════════════════════════════════════
    @Test
    void chatRequest_equalsSubclassCanEqualFalse() {
        ChatRequest a = new ChatRequest("q");
        ChatRequest sub = new ChatRequest("q") {
            @Override public boolean canEqual(Object other) { return false; }
        };
        // a.equals(sub) calls sub.canEqual(a) → false → not equal
        assertNotEquals(a, sub);
    }

    @Test
    void chatResponse_equalsSubclassCanEqualFalse() {
        ChatResponse a = ChatResponse.builder().response("r").timestamp("t").build();
        ChatResponse sub = new ChatResponse("r", "t") {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void electionStep_equalsSubclassCanEqualFalse() {
        ElectionStepResponse a = new ElectionStepResponse("1", "T", "D", "Desc", true);
        ElectionStepResponse sub = new ElectionStepResponse("1", "T", "D", "Desc", true) {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void analytics_equalsSubclassCanEqualFalse() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsSnapshotDto a = new AnalyticsSnapshotDto("s", "a", now, "m");
        AnalyticsSnapshotDto sub = new AnalyticsSnapshotDto("s", "a", now, "m") {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void health_equalsSubclassCanEqualFalse() {
        HealthResponse a = new HealthResponse("UP", "OK", "1.0");
        HealthResponse sub = new HealthResponse("UP", "OK", "1.0") {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void error_equalsSubclassCanEqualFalse() {
        Map<String, String> d = Map.of("k", "v");
        ErrorResponse a = new ErrorResponse("E", "M", d);
        ErrorResponse sub = new ErrorResponse("E", "M", d) {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void timeline_equalsSubclassCanEqualFalse() {
        List<ElectionStepResponse> steps = Collections.emptyList();
        TimelineResponse a = new TimelineResponse(steps, 0);
        TimelineResponse sub = new TimelineResponse(steps, 0) {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void stateList_equalsSubclassCanEqualFalse() {
        List<StateElection> states = Collections.emptyList();
        StateListResponse a = new StateListResponse(states, 0);
        StateListResponse sub = new StateListResponse(states, 0) {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }
}
