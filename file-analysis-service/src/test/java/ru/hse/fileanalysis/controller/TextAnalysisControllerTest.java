package ru.hse.fileanalysis.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.hse.fileanalysis.dto.SimilarFileDto;
import ru.hse.fileanalysis.dto.SimilarityResultDto;
import ru.hse.fileanalysis.dto.TextStatisticsDto;
import ru.hse.fileanalysis.exception.FileServiceException;
import ru.hse.fileanalysis.service.TextAnalysisService;

@WebMvcTest(TextAnalysisController.class)
public class TextAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextAnalysisService textAnalysisService;

    private String fileId;
    private TextStatisticsDto textStatisticsDto;
    private SimilarityResultDto similarityResultDto;

    @BeforeEach
    public void setup() {
        fileId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        textStatisticsDto = TextStatisticsDto.builder()
                .fileId(fileId)
                .paragraphs(3)
                .words(100)
                .characters(500)
                .analysisDate(now)
                .build();
        
        List<SimilarFileDto> similarFiles = new ArrayList<>();
        similarFiles.add(SimilarFileDto.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName("similar.txt")
                .similarityScore(75.0)
                .build());
        
        similarityResultDto = SimilarityResultDto.builder()
                .fileId(fileId)
                .similarFiles(similarFiles)
                .analysisDate(now)
                .build();
    }

    @Test
    public void testAnalyzeTextStatistics() throws Exception {
        // Arrange
        when(textAnalysisService.analyzeTextStatistics(fileId)).thenReturn(textStatisticsDto);

        // Act & Assert
        mockMvc.perform(post("/api/analysis/stats/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId", is(fileId)))
                .andExpect(jsonPath("$.paragraphs", is(3)))
                .andExpect(jsonPath("$.words", is(100)))
                .andExpect(jsonPath("$.characters", is(500)));
    }

    @Test
    public void testAnalyzeSimilarity() throws Exception {
        // Arrange
        when(textAnalysisService.analyzeSimilarity(fileId)).thenReturn(similarityResultDto);

        // Act & Assert
        mockMvc.perform(post("/api/analysis/similarity/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId", is(fileId)))
                .andExpect(jsonPath("$.similarFiles[0].fileName", is("similar.txt")))
                .andExpect(jsonPath("$.similarFiles[0].similarityScore", is(75.0)));
    }

    @Test
    public void testGetWordCloud() throws Exception {
        // Arrange
        byte[] imageData = "Test image data".getBytes();
        Resource wordCloudResource = new ByteArrayResource(imageData);
        
        when(textAnalysisService.generateWordCloud(eq(fileId), anyString(), anyInt(), anyInt()))
                .thenReturn(wordCloudResource);

        // Act & Assert
        mockMvc.perform(get("/api/analysis/wordcloud/{fileId}", fileId)
                .param("format", "svg")
                .param("width", "800")
                .param("height", "600"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/svg+xml"))
                .andExpect(content().bytes(imageData));
    }

    @Test
    public void testGetTextStatistics() throws Exception {
        // Arrange
        when(textAnalysisService.getTextStatistics(fileId)).thenReturn(textStatisticsDto);

        // Act & Assert
        mockMvc.perform(get("/api/analysis/stats/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId", is(fileId)))
                .andExpect(jsonPath("$.paragraphs", is(3)))
                .andExpect(jsonPath("$.words", is(100)))
                .andExpect(jsonPath("$.characters", is(500)));
    }

    @Test
    public void testGetTextStatistics_NotFound() throws Exception {
        // Arrange
        when(textAnalysisService.getTextStatistics(fileId))
                .thenThrow(new FileServiceException("Statistics not found"));

        // Act & Assert
        mockMvc.perform(get("/api/analysis/stats/{fileId}", fileId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSimilarityResult() throws Exception {
        // Arrange
        when(textAnalysisService.getSimilarityResult(fileId)).thenReturn(similarityResultDto);

        // Act & Assert
        mockMvc.perform(get("/api/analysis/similarity/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId", is(fileId)))
                .andExpect(jsonPath("$.similarFiles[0].fileName", is("similar.txt")))
                .andExpect(jsonPath("$.similarFiles[0].similarityScore", is(75.0)));
    }

    @Test
    public void testGetSimilarityResult_NotFound() throws Exception {
        // Arrange
        when(textAnalysisService.getSimilarityResult(fileId))
                .thenThrow(new FileServiceException("Similarity result not found"));

        // Act & Assert
        mockMvc.perform(get("/api/analysis/similarity/{fileId}", fileId))
                .andExpect(status().isNotFound());
    }
} 