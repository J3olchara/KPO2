package ru.hse.fileanalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.hse.fileanalysis.client.FileMetadata;
import ru.hse.fileanalysis.client.FileStorageClient;
import ru.hse.fileanalysis.dto.SimilarFileDto;
import ru.hse.fileanalysis.dto.SimilarityResultDto;
import ru.hse.fileanalysis.dto.TextStatisticsDto;
import ru.hse.fileanalysis.exception.FileServiceException;
import ru.hse.fileanalysis.model.SimilarFile;
import ru.hse.fileanalysis.model.SimilarityResult;
import ru.hse.fileanalysis.model.TextStatistics;
import ru.hse.fileanalysis.repository.SimilarityResultRepository;
import ru.hse.fileanalysis.repository.TextStatisticsRepository;
import ru.hse.fileanalysis.service.impl.TextAnalysisServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TextAnalysisServiceTest {

    @Mock
    private FileStorageClient fileStorageClient;

    @Mock
    private TextStatisticsRepository textStatisticsRepository;

    @Mock
    private SimilarityResultRepository similarityResultRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;
    
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private TextAnalysisServiceImpl textAnalysisService;

    private String fileId;
    private String fileContent;
    private TextStatistics textStatistics;
    private SimilarityResult similarityResult;

    @BeforeEach
    public void setup() {
        fileId = UUID.randomUUID().toString();
        fileContent = "This is a test file content.\n\nIt has multiple paragraphs.\n\nAnd some words.";
        
        textStatistics = TextStatistics.builder()
                .fileId(fileId)
                .paragraphs(3)
                .words(13)
                .characters(fileContent.length())
                .analysisDate(LocalDateTime.now())
                .build();
        
        List<SimilarFile> similarFiles = new ArrayList<>();
        similarityResult = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(similarFiles)
                .analysisDate(LocalDateTime.now())
                .build();
        
        SimilarFile similarFile = SimilarFile.builder()
                .id(1L)
                .similarityResult(similarityResult)
                .fileId(UUID.randomUUID().toString())
                .fileName("similar.txt")
                .similarityScore(75.0)
                .build();
        
        similarFiles.add(similarFile);
    }

    @Test
    public void testAnalyzeTextStatistics_Success() {
        // Arrange
        when(fileStorageClient.getFileContent(fileId)).thenReturn(Mono.just(fileContent));
        when(textStatisticsRepository.save(any(TextStatistics.class))).thenReturn(textStatistics);

        // Act
        TextStatisticsDto result = textAnalysisService.analyzeTextStatistics(fileId);

        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getFileId());
        assertEquals(3, result.getParagraphs());
        assertEquals(13, result.getWords());
        assertEquals(fileContent.length(), result.getCharacters());
        
        verify(fileStorageClient, times(1)).getFileContent(fileId);
        verify(textStatisticsRepository, times(1)).save(any(TextStatistics.class));
    }

    @Test
    public void testAnalyzeSimilarity_Success() {
        // Arrange
        FileMetadata[] files = {
            FileMetadata.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName("file1.txt")
                .size(100L)
                .uploadDate(LocalDateTime.now())
                .build(),
            FileMetadata.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName("file2.txt")
                .size(200L)
                .uploadDate(LocalDateTime.now())
                .build()
        };
        
        String otherFileContent = "This is a very similar test file content.\n\nIt also has multiple paragraphs.\n\nAnd some words.";

        when(fileStorageClient.getFileContent(fileId)).thenReturn(Mono.just(fileContent));
        when(fileStorageClient.getAllFiles()).thenReturn(Mono.just(files));
        when(fileStorageClient.getFileContent(files[0].getFileId())).thenReturn(Mono.just(otherFileContent));
        when(fileStorageClient.getFileContent(files[1].getFileId())).thenReturn(Mono.just("Completely different content"));
        when(similarityResultRepository.save(any(SimilarityResult.class))).thenReturn(similarityResult);

        // Act
        SimilarityResultDto result = textAnalysisService.analyzeSimilarity(fileId);

        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getFileId());
        
        verify(fileStorageClient, times(1)).getFileContent(fileId);
        verify(fileStorageClient, times(1)).getAllFiles();
        verify(similarityResultRepository, times(1)).save(any(SimilarityResult.class));
    }

    @Test
    public void testAnalyzeSimilarity_FileContentNotFound() {
        // Arrange
        when(fileStorageClient.getFileContent(fileId)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(FileServiceException.class, () -> textAnalysisService.analyzeSimilarity(fileId));
        
        verify(fileStorageClient, times(1)).getFileContent(fileId);
    }

    @Test
    public void testGetTextStatistics_Success() {
        // Arrange
        when(textStatisticsRepository.findById(fileId)).thenReturn(Optional.of(textStatistics));

        // Act
        TextStatisticsDto result = textAnalysisService.getTextStatistics(fileId);

        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getFileId());
        assertEquals(3, result.getParagraphs());
        assertEquals(13, result.getWords());
        
        verify(textStatisticsRepository, times(1)).findById(fileId);
    }

    @Test
    public void testGetTextStatistics_NotFound() {
        // Arrange
        when(textStatisticsRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileServiceException.class, () -> textAnalysisService.getTextStatistics(fileId));
        
        verify(textStatisticsRepository, times(1)).findById(fileId);
    }

    @Test
    public void testGetSimilarityResult_Success() {
        // Arrange
        when(similarityResultRepository.findById(fileId)).thenReturn(Optional.of(similarityResult));

        // Act
        SimilarityResultDto result = textAnalysisService.getSimilarityResult(fileId);

        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getFileId());
        assertEquals(1, result.getSimilarFiles().size());
        assertEquals("similar.txt", result.getSimilarFiles().get(0).getFileName());
        assertEquals(75.0, result.getSimilarFiles().get(0).getSimilarityScore());
        
        verify(similarityResultRepository, times(1)).findById(fileId);
    }

    @Test
    public void testGetSimilarityResult_NotFound() {
        // Arrange
        when(similarityResultRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileServiceException.class, () -> textAnalysisService.getSimilarityResult(fileId));
        
        verify(similarityResultRepository, times(1)).findById(fileId);
    }

    @Test
    public void testGenerateWordCloud_Success() {
        // Arrange
        byte[] wordCloudData = "Word Cloud Image Data".getBytes();
        
        // Создаем настоящие моки для каждого шага цепочки WebClient
        WebClient mockWebClient = mock(WebClient.class);
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);
        
        // Настраиваем их поведение
        when(webClientBuilder.build()).thenReturn(mockWebClient);
        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri(anyString())).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.contentType(any())).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.bodyValue(any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(wordCloudData));
        
        when(fileStorageClient.getFileContent(fileId)).thenReturn(Mono.just(fileContent));

        // Создаем новый экземпляр сервиса с настроенными моками
        TextAnalysisServiceImpl service = new TextAnalysisServiceImpl(
            fileStorageClient,
            textStatisticsRepository,
            similarityResultRepository,
            webClientBuilder
        );
        
        // Устанавливаем URL для API облака слов через reflection
        try {
            java.lang.reflect.Field field = TextAnalysisServiceImpl.class.getDeclaredField("wordCloudApiUrl");
            field.setAccessible(true);
            field.set(service, "http://wordcloud-api.example.com/generate");
        } catch (Exception e) {
            fail("Не удалось установить URL для API облака слов: " + e.getMessage());
        }

        // Act
        Resource result = service.generateWordCloud(fileId, "svg", 800, 600);

        // Assert
        assertNotNull(result);
        
        verify(fileStorageClient, times(1)).getFileContent(fileId);
        verify(mockWebClient, times(1)).post();
    }

    @Test
    public void testGenerateWordCloud_FileContentNotFound() {
        // Arrange
        when(fileStorageClient.getFileContent(fileId)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(FileServiceException.class, () -> textAnalysisService.generateWordCloud(fileId, "svg", 800, 600));
        
        verify(fileStorageClient, times(1)).getFileContent(fileId);
    }
} 