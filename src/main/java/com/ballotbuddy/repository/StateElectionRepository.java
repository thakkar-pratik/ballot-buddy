package com.ballotbuddy.repository;

import com.ballotbuddy.entity.StateElection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateElectionRepository extends JpaRepository<StateElection, Long> {
    Optional<StateElection> findByStateNameIgnoreCase(String stateName);
}
