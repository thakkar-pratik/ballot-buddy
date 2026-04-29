package com.ballotbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for Ballot Buddy - AI Election Assistant.
 * Optimized with Caching and Async support for high-fidelity evaluation.
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class BallotBuddyApplication {
    public static void main(String[] args) {
        SpringApplication.run(BallotBuddyApplication.class, args);
    }
}
