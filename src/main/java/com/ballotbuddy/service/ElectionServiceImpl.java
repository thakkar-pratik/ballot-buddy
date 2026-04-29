package com.ballotbuddy.service;

import com.ballotbuddy.dto.ElectionStepResponse;
import com.ballotbuddy.entity.StateElection;
import com.ballotbuddy.repository.StateElectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ElectionService} providing election timelines and state data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ElectionServiceImpl implements ElectionService {

    private final StateElectionRepository stateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElectionStepResponse> getTimeline() {
        log.debug("Fetching election timeline steps");
        List<ElectionStepResponse> timeline = new ArrayList<>();
        
        timeline.add(ElectionStepResponse.builder()
                .id("1")
                .title("Voter Registration")
                .date("Ongoing - Ends Oct 15")
                .description("Ensure you are registered to vote in your local jurisdiction. Check your status online.")
                .completed(true)
                .build());

        timeline.add(ElectionStepResponse.builder()
                .id("2")
                .title("Early Voting Begins")
                .date("Oct 24")
                .description("Many states allow you to cast your ballot early in person or via mail.")
                .completed(false)
                .build());

        timeline.add(ElectionStepResponse.builder()
                .id("3")
                .title("Election Day")
                .date("Nov 5")
                .description("The big day! Polls are open from 7 AM to 8 PM. Bring a valid ID if required.")
                .completed(false)
                .build());

        timeline.add(ElectionStepResponse.builder()
                .id("4")
                .title("Results Counting")
                .date("Nov 5 - Nov 12")
                .description("Election officials count every legal ballot to determine the winner.")
                .completed(false)
                .build());

        return timeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StateElection> getAllStates() {
        log.debug("Fetching all state-wise election data from H2");
        return stateRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimelineContext() {
        StringBuilder context = new StringBuilder("General Election Timeline Context:\n");
        getTimeline().forEach(step -> context.append("- ")
                .append(step.getTitle())
                .append(" on ")
                .append(step.getDate())
                .append(": ")
                .append(step.getDescription())
                .append("\n"));
        
        context.append("\nState-wise Election Data (India):\n");
        stateRepository.findAll().forEach(state -> {
            context.append(String.format("- %s: %d voters, Parties: %s, Candidates: %s, Next Election: %s\n",
                state.getStateName(), state.getVoterCount(), state.getParties(), state.getMainParticipants(), state.getElectionDate()));
        });
        
        return context.toString();
    }
}
