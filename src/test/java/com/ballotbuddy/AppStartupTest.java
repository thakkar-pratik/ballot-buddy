package com.ballotbuddy;

import com.ballotbuddy.config.DataInitializer;
import com.ballotbuddy.repository.StateElectionRepository;
import com.google.cloud.logging.Logging;
import com.google.cloud.storage.Storage;
import com.google.cloud.vertexai.VertexAI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AppStartupTest {

    @MockBean
    private Storage storage;

    @MockBean
    private Logging logging;

    @MockBean
    private VertexAI vertexAI;

    @MockBean
    private com.google.firebase.auth.FirebaseAuth firebaseAuth;

    @Test
    void contextLoads() {
        // Verifies that the application context starts successfully
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
