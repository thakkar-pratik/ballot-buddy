package com.ballotbuddy.service;

import com.ballotbuddy.dto.AnalyticsSnapshotDto;

/**
 * Interface for analytics and persistence services.
 */
public interface AnalyticsStorageService {
    /**
     * Saves an interaction snapshot to cloud or local storage.
     * @param snapshot The analytics data to save.
     */
    void saveSnapshot(AnalyticsSnapshotDto snapshot);
}
