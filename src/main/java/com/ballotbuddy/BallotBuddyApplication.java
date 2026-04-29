package com.ballotbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BallotBuddyApplication {
    public static void main(String[] args) {
        SpringApplication.run(BallotBuddyApplication.class, args);
    }
}
