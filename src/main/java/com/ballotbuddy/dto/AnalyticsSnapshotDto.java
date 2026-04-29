package com.ballotbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSnapshotDto {
    private String sessionId;
    private String action;
    private LocalDateTime timestamp;
    private String metadata;
}
