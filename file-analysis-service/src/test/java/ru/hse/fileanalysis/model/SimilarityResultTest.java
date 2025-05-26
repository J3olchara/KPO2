package ru.hse.fileanalysis.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SimilarityResultTest {

    @Test
    public void testSimilarityResultBuilder() {
        // Arrange
        String fileId = "test-id";
        List<SimilarFile> similarFiles = new ArrayList<>();
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        SimilarityResult result = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(similarFiles)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getFileId());
        assertEquals(similarFiles, result.getSimilarFiles());
        assertEquals(analysisDate, result.getAnalysisDate());
    }

    @Test
    public void testSimilarityResultSettersAndGetters() {
        // Arrange
        SimilarityResult result = new SimilarityResult();
        String fileId = "test-id";
        List<SimilarFile> similarFiles = new ArrayList<>();
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        result.setFileId(fileId);
        result.setSimilarFiles(similarFiles);
        result.setAnalysisDate(analysisDate);

        // Assert
        assertEquals(fileId, result.getFileId());
        assertEquals(similarFiles, result.getSimilarFiles());
        assertEquals(analysisDate, result.getAnalysisDate());
    }

    @Test
    public void testSimilarityResultWithSimilarFiles() {
        // Arrange
        String fileId = "test-id";
        List<SimilarFile> similarFiles = new ArrayList<>();
        LocalDateTime analysisDate = LocalDateTime.now();

        SimilarityResult result = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(similarFiles)
                .analysisDate(analysisDate)
                .build();

        // Add a similar file
        SimilarFile similarFile = SimilarFile.builder()
                .id(1L)
                .similarityResult(result)
                .fileId("similar-file-id")
                .fileName("similar.txt")
                .similarityScore(75.0)
                .build();
        
        similarFiles.add(similarFile);

        // Assert
        assertEquals(1, result.getSimilarFiles().size());
        assertEquals("similar-file-id", result.getSimilarFiles().get(0).getFileId());
        assertEquals("similar.txt", result.getSimilarFiles().get(0).getFileName());
        assertEquals(75.0, result.getSimilarFiles().get(0).getSimilarityScore());
    }

    @Test
    public void testSimilarityResultEqualsAndHashCode() {
        // Arrange
        String fileId = "test-id";
        List<SimilarFile> similarFiles1 = new ArrayList<>();
        List<SimilarFile> similarFiles2 = new ArrayList<>();
        LocalDateTime analysisDate = LocalDateTime.now();

        SimilarityResult result1 = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(similarFiles1)
                .analysisDate(analysisDate)
                .build();

        SimilarityResult result2 = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(similarFiles2)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }
} 