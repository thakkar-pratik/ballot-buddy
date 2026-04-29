package com.ballotbuddy.service;

import com.ballotbuddy.dto.AnalyticsSnapshotDto;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Implementation of {@link AnalyticsStorageService} using Google Cloud Storage.
 */
@Slf4j
@Service
public class AnalyticsStorageServiceImpl implements AnalyticsStorageService {

    private static final String BLOB_PREFIX = "analytics/";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String FORMAT_CONTENT = "Session: %s, Action: %s, Time: %s, Meta: %s";

    @Value("${gcp.storage.bucket}")
    private String bucketName;

    @Value("${gcp.project.id}")
    private String projectId;

    private Storage storage;

    /**
     * Initializes the Storage service. Can be overridden in tests.
     */
    @PostConstruct
    protected void init() {
        try {
            this.storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        } catch (RuntimeException e) {
            log.warn("Could not initialize GCP Storage: {}", e.getMessage());
        }
    }

    /**
     * Set storage manually for testing.
     */
    protected void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void saveSnapshot(AnalyticsSnapshotDto snapshot) {
        String content = String.format(FORMAT_CONTENT,
                snapshot.getSessionId(), snapshot.getAction(), snapshot.getTimestamp(), snapshot.getMetadata());

        try {
            if (storage == null) {
                throw new IllegalStateException("Storage service not initialized");
            }
            String blobName = BLOB_PREFIX + UUID.randomUUID() + ".txt";
            BlobId blobId = BlobId.of(bucketName, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(CONTENT_TYPE).build();
            
            storage.create(blobInfo, content.getBytes(StandardCharsets.UTF_8));
            log.info("Analytics snapshot saved to GCP: {}", blobName);
        } catch (RuntimeException e) {
            log.warn("GCP Storage unavailable. Falling back to local logging. Error: {}", e.getMessage());
            log.info("[LOCAL ANALYTICS] {}", content);
        }
    }
}
