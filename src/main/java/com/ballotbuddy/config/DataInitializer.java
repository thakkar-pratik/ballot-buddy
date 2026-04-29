package com.ballotbuddy.config;

import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class that populates the H2 in-memory database with initial election data.
 * Used for local intelligence fallback mechanisms.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final StateElectionRepository repository;

    @Override
    public void run(String... args) {
        log.info("Initializing Indian State Election data in H2...");
        
        List<StateElection> states = List.of(
            StateElection.builder()
                .stateName("Maharashtra")
                .voterCount(96000000L)
                .parties("BJP, Shiv Sena, NCP, INC")
                .mainParticipants("Eknath Shinde, Devendra Fadnavis, Uddhav Thackeray")
                .electionDate("Nov 2024")
                .currentStatus("Pre-election preparation")
                .build(),
            StateElection.builder()
                .stateName("Uttar Pradesh")
                .voterCount(150000000L)
                .parties("BJP, SP, BSP, INC")
                .mainParticipants("Yogi Adityanath, Akhilesh Yadav, Mayawati")
                .electionDate("Feb 2027")
                .currentStatus("Regular monitoring")
                .build(),
            StateElection.builder()
                .stateName("Karnataka")
                .voterCount(53000000L)
                .parties("INC, BJP, JD(S)")
                .mainParticipants("Siddaramaiah, D.K. Shivakumar, B.Y. Vijayendra")
                .electionDate("May 2028")
                .currentStatus("Governance Phase")
                .build(),
            StateElection.builder()
                .stateName("Delhi")
                .voterCount(15000000L)
                .parties("AAP, BJP, INC")
                .mainParticipants("Arvind Kejriwal, Atishi Marlena")
                .electionDate("Feb 2025")
                .currentStatus("Election cycle starting soon")
                .build()
        );

        repository.saveAll(states);
        log.info("H2 Database initialized with {} states.", states.size());
    }
}
