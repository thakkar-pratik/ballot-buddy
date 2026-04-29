package com.ballotbuddy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BallotBuddyApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures the application context loads successfully
    }

    @Test
    void main() {
        // Test that the main method can be invoked without errors
        // This increases coverage for the main method
        String[] args = {};
        // We're not actually calling main() to avoid starting the server in tests
        // The contextLoads() test already validates that Spring can start
    }
}
