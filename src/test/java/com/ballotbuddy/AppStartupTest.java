package com.ballotbuddy;

import com.ballotbuddy.config.DataInitializer;
import com.ballotbuddy.repository.StateElectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class AppStartupTest {

    @Test
    void testMain() {
        // Exercise the main application entry point
        BallotBuddyApplication.main(new String[]{"--server.port=0"});
    }

    @Test
    void testDataInitializer() throws Exception {
        StateElectionRepository repo = mock(StateElectionRepository.class);
        DataInitializer initializer = new DataInitializer(repo);
        
        initializer.run();
        
        // Verify that the database was seeded
        verify(repo, atLeastOnce()).saveAll(anyList());
    }
}
