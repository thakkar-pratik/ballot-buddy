package com.ballotbuddy.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateElectionTest {

    @Test
    void stateElection_AllMethods() {
        StateElection entity = StateElection.builder()
                .id(1L)
                .stateName("Maharashtra")
                .voterCount(96000000L)
                .parties("BJP, Shiv Sena")
                .mainParticipants("Shinde, Fadnavis")
                .electionDate("2024-11-20")
                .currentStatus("ACTIVE")
                .build();
        
        assertEquals(1L, entity.getId());
        assertEquals("Maharashtra", entity.getStateName());
        assertEquals(96000000L, entity.getVoterCount());
        assertEquals("BJP, Shiv Sena", entity.getParties());
        assertEquals("Shinde, Fadnavis", entity.getMainParticipants());
        assertEquals("2024-11-20", entity.getElectionDate());
        assertEquals("ACTIVE", entity.getCurrentStatus());
        
        StateElection entity2 = new StateElection();
        entity2.setId(2L);
        entity2.setStateName("Delhi");
        entity2.setVoterCount(15000000L);
        entity2.setParties("AAP, BJP");
        entity2.setMainParticipants("Kejriwal");
        entity2.setElectionDate("2025-02-15");
        entity2.setCurrentStatus("SCHEDULED");
        
        assertNotNull(entity.toString());
        assertEquals(entity, entity);
        assertNotEquals(entity, entity2);
        assertNotEquals(entity, null);
        assertNotEquals(entity, "string");
        assertTrue(entity.hashCode() != 0);
    }

    @Test
    void stateElection_NoArgsConstructor() {
        StateElection entity = new StateElection();
        assertNull(entity.getId());
        assertNull(entity.getStateName());
    }

    @Test
    void stateElection_AllArgsConstructor() {
        StateElection entity = new StateElection(
            1L, "UP", 150000000L, "BJP", "Yogi", "2024-12-01", "ACTIVE"
        );
        
        assertEquals("UP", entity.getStateName());
        assertEquals(150000000L, entity.getVoterCount());
    }
}
