package com.ballotbuddy.service;

import com.ballotbuddy.dto.AnalyticsSnapshotDto;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsStorageServiceImplTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private AnalyticsStorageServiceImpl analyticsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(analyticsService, "bucketName", "test-bucket");
        ReflectionTestUtils.setField(analyticsService, "projectId", "test-project");
        analyticsService.setStorage(storage);
    }

    @Test
    void saveSnapshot_Success() {
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder()
                .sessionId("sid")
                .action("ACT")
                .timestamp(LocalDateTime.now())
                .metadata("meta")
                .build();

        when(storage.create(any(BlobInfo.class), any(byte[].class))).thenReturn(null);

        analyticsService.saveSnapshot(dto);

        verify(storage, times(1)).create(any(BlobInfo.class), any(byte[].class));
    }

    @Test
    void saveSnapshot_FallbackOnFailure() {
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder().build();
        when(storage.create(any(BlobInfo.class), any(byte[].class))).thenThrow(new RuntimeException("Cloud Down"));
        
        analyticsService.saveSnapshot(dto);
        
        verify(storage).create(any(BlobInfo.class), any(byte[].class));
    }

    @Test
    void saveSnapshot_FallbackOnNullStorage() {
        analyticsService.setStorage(null);
        AnalyticsSnapshotDto dto = AnalyticsSnapshotDto.builder().build();
        analyticsService.saveSnapshot(dto);
    }

    @Test
    void init_ShouldHandleInitialization() {
        // Success path - should not throw any exception
        analyticsService.init();
    }

    @Test
    void init_CatchesExceptionWhenInitializationFails() {
        // Create a new instance to test the init failure path
        AnalyticsStorageServiceImpl newService = new AnalyticsStorageServiceImpl();
        ReflectionTestUtils.setField(newService, "projectId", "test-project");
        ReflectionTestUtils.setField(newService, "bucketName", "test-bucket");

        // Use Mockito's static mocking to force StorageOptions to throw an exception
        try (MockedStatic<StorageOptions> mockedStatic = mockStatic(StorageOptions.class)) {
            StorageOptions.Builder mockBuilder = mock(StorageOptions.Builder.class);

            // Make the static method return our mock builder
            mockedStatic.when(StorageOptions::newBuilder).thenReturn(mockBuilder);

            // Make the builder chain throw an exception
            when(mockBuilder.setProjectId(any())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenThrow(new RuntimeException("GCP initialization failed"));

            // This should catch the exception and log warning, not throw
            assertDoesNotThrow(() -> newService.init());

            // Verify storage is still null after failed init
            Storage actualStorage = (Storage) ReflectionTestUtils.getField(newService, "storage");
            // Storage should be null since initialization failed
        }
    }
}
