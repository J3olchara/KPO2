package ru.hse.fileanalysis.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TextStatisticsTest {

    @Test
    public void testTextStatisticsBuilder() {
        // Arrange
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        TextStatistics stats = TextStatistics.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertNotNull(stats);
        assertEquals(fileId, stats.getFileId());
        assertEquals(paragraphs, stats.getParagraphs());
        assertEquals(words, stats.getWords());
        assertEquals(characters, stats.getCharacters());
        assertEquals(analysisDate, stats.getAnalysisDate());
    }

    @Test
    public void testTextStatisticsSettersAndGetters() {
        // Arrange
        TextStatistics stats = new TextStatistics();
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        stats.setFileId(fileId);
        stats.setParagraphs(paragraphs);
        stats.setWords(words);
        stats.setCharacters(characters);
        stats.setAnalysisDate(analysisDate);

        // Assert
        assertEquals(fileId, stats.getFileId());
        assertEquals(paragraphs, stats.getParagraphs());
        assertEquals(words, stats.getWords());
        assertEquals(characters, stats.getCharacters());
        assertEquals(analysisDate, stats.getAnalysisDate());
    }

    @Test
    public void testTextStatisticsEqualsAndHashCode() {
        // Arrange
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        TextStatistics stats1 = TextStatistics.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        TextStatistics stats2 = TextStatistics.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertEquals(stats1, stats2);
        assertEquals(stats1.hashCode(), stats2.hashCode());
    }
} 