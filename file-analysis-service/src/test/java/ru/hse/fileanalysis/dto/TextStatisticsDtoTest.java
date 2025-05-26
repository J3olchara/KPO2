package ru.hse.fileanalysis.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TextStatisticsDtoTest {

    @Test
    public void testTextStatisticsDtoBuilder() {
        // Arrange
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        TextStatisticsDto dto = TextStatisticsDto.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(fileId, dto.getFileId());
        assertEquals(paragraphs, dto.getParagraphs());
        assertEquals(words, dto.getWords());
        assertEquals(characters, dto.getCharacters());
        assertEquals(analysisDate, dto.getAnalysisDate());
    }

    @Test
    public void testTextStatisticsDtoSettersAndGetters() {
        // Arrange
        TextStatisticsDto dto = new TextStatisticsDto();
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        // Act
        dto.setFileId(fileId);
        dto.setParagraphs(paragraphs);
        dto.setWords(words);
        dto.setCharacters(characters);
        dto.setAnalysisDate(analysisDate);

        // Assert
        assertEquals(fileId, dto.getFileId());
        assertEquals(paragraphs, dto.getParagraphs());
        assertEquals(words, dto.getWords());
        assertEquals(characters, dto.getCharacters());
        assertEquals(analysisDate, dto.getAnalysisDate());
    }

    @Test
    public void testTextStatisticsDtoEqualsAndHashCode() {
        // Arrange
        String fileId = "test-id";
        Integer paragraphs = 3;
        Integer words = 100;
        Integer characters = 500;
        LocalDateTime analysisDate = LocalDateTime.now();

        TextStatisticsDto dto1 = TextStatisticsDto.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        TextStatisticsDto dto2 = TextStatisticsDto.builder()
                .fileId(fileId)
                .paragraphs(paragraphs)
                .words(words)
                .characters(characters)
                .analysisDate(analysisDate)
                .build();

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
} 