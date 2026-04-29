package com.ballotbuddy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 100% coverage for StateElection entity.
 * Exercises all Lombok-generated methods:
 * getters, setters, constructors, builder, equals, hashCode, toString, canEqual.
 */
class StateElectionTest {

    @Test
    void stateElection_fullCoverage() {
        StateElection entity = new StateElection();
        entity.setId(1L);
        entity.setStateName("Maharashtra");
        entity.setVoterCount(1000000L);
        entity.setParties("Party A, Party B");
        entity.setMainParticipants("Candidate X, Candidate Y");
        entity.setElectionDate("2024-05-20");
        entity.setCurrentStatus("ONGOING");

        assertEquals(1L, entity.getId());
        assertEquals("Maharashtra", entity.getStateName());
        assertEquals(1000000L, entity.getVoterCount());
        assertEquals("Party A, Party B", entity.getParties());
        assertEquals("Candidate X, Candidate Y", entity.getMainParticipants());
        assertEquals("2024-05-20", entity.getElectionDate());
        assertEquals("ONGOING", entity.getCurrentStatus());

        assertNotNull(entity.toString());
        assertEquals(entity, entity);
        assertNotEquals(entity, null);
        assertNotEquals(entity, "string");
        
        StateElection entity2 = StateElection.builder()
                .id(1L)
                .stateName("Maharashtra")
                .voterCount(1000000L)
                .parties("Party A, Party B")
                .mainParticipants("Candidate X, Candidate Y")
                .electionDate("2024-05-20")
                .currentStatus("ONGOING")
                .build();
        
        assertEquals(entity, entity2);
        assertEquals(entity.hashCode(), entity2.hashCode());
        assertTrue(entity.canEqual(entity2));

        entity2.setStateName("Goa");
        assertNotEquals(entity, entity2);
        assertNotEquals(entity.hashCode(), entity2.hashCode());
    }

    @Test
    void stateElection_allArgConstructor() {
        StateElection entity = new StateElection(1L, "S", 10L, "P", "M", "D", "C");
        assertEquals(1L, entity.getId());
        assertEquals("S", entity.getStateName());
    }

    @Test
    void stateElection_builderToString() {
        String s = StateElection.builder().stateName("Test").toString();
        assertNotNull(s);
        assertTrue(s.contains("Test"));
    }

    @Test
    void stateElection_equalsSubclassCanEqualFalse() {
        StateElection a = new StateElection();
        StateElection sub = new StateElection() {
            @Override public boolean canEqual(Object other) { return false; }
        };
        assertNotEquals(a, sub);
    }

    @Test
    void stateElection_exhaustiveEqualsAndHashCode() {
        // ID
        StateElection a = StateElection.builder().id(1L).build();
        StateElection b = StateElection.builder().id(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // stateName
        a = StateElection.builder().stateName("S").build();
        b = StateElection.builder().stateName(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // voterCount
        a = StateElection.builder().voterCount(10L).build();
        b = StateElection.builder().voterCount(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // parties
        a = StateElection.builder().parties("P").build();
        b = StateElection.builder().parties(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // mainParticipants
        a = StateElection.builder().mainParticipants("M").build();
        b = StateElection.builder().mainParticipants(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // electionDate
        a = StateElection.builder().electionDate("D").build();
        b = StateElection.builder().electionDate(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());

        // currentStatus
        a = StateElection.builder().currentStatus("C").build();
        b = StateElection.builder().currentStatus(null).build();
        assertNotEquals(a, b);
        assertNotEquals(b, a);
        assertNotEquals(a.hashCode(), b.hashCode());
        
        // Both null (equality branch)
        StateElection c1 = new StateElection();
        StateElection c2 = new StateElection();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
