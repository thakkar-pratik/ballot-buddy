package com.ballotbuddy.controller;

import com.ballotbuddy.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for application health monitoring.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    private static final String STATUS_UP = "UP";
    private static final String HEALTH_MESSAGE = "Ballot Buddy is ready to help!";
    private static final String APP_VERSION = "1.0.0-PROD";

    /**
     * Simple health check endpoint.
     * @return HealthResponse DTO.
     */
    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(HealthResponse.builder()
                .status(STATUS_UP)
                .message(HEALTH_MESSAGE)
                .version(APP_VERSION)
                .build());
    }
}
