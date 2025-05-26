package ru.hse.fileanalysis.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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
import ru.hse.fileanalysis.service.TextAnalysisService;

@Service
public class TextAnalysisServiceImpl implements TextAnalysisService {

    private final FileStorageClient fileStorageClient;
    private final TextStatisticsRepository textStatisticsRepository;
    private final SimilarityResultRepository similarityResultRepository;
    private final WebClient webClient;
    
    @Value("${wordcloud.api.url}")
    private String wordCloudApiUrl;
    
    @Autowired
    public TextAnalysisServiceImpl(FileStorageClient fileStorageClient,
                                TextStatisticsRepository textStatisticsRepository,
                                SimilarityResultRepository similarityResultRepository,
                                WebClient.Builder webClientBuilder) {
        this.fileStorageClient = fileStorageClient;
        this.textStatisticsRepository = textStatisticsRepository;
        this.similarityResultRepository = similarityResultRepository;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public TextStatisticsDto analyzeTextStatistics(String fileId) {
        return fileStorageClient.getFileContent(fileId)
                .map(content -> {
                    // Анализируем статистику текста
                    int paragraphs = countParagraphs(content);
                    int words = countWords(content);
                    int characters = content.length();
                    
                    // Сохраняем в БД
                    TextStatistics stats = TextStatistics.builder()
                            .fileId(fileId)
                            .paragraphs(paragraphs)
                            .words(words)
                            .characters(characters)
                            .analysisDate(LocalDateTime.now())
                            .build();
                    
                    textStatisticsRepository.save(stats);
                    
                    // Возвращаем DTO
                    return TextStatisticsDto.builder()
                            .fileId(stats.getFileId())
                            .paragraphs(stats.getParagraphs())
                            .words(stats.getWords())
                            .characters(stats.getCharacters())
                            .analysisDate(stats.getAnalysisDate())
                            .build();
                })
                .block();
    }

    @Override
    public SimilarityResultDto analyzeSimilarity(String fileId) {
        // Получаем содержимое текущего файла
        String currentFileContent = fileStorageClient.getFileContent(fileId).block();
        
        if (currentFileContent == null) {
            throw new FileServiceException("Не удалось получить содержимое файла с id: " + fileId);
        }
        
        // Получаем список всех файлов
        FileMetadata[] allFiles = fileStorageClient.getAllFiles().block();
        
        if (allFiles == null) {
            throw new FileServiceException("Не удалось получить список файлов");
        }
        
        // Создаем результат анализа
        SimilarityResult similarityResult = SimilarityResult.builder()
                .fileId(fileId)
                .similarFiles(new ArrayList<>())
                .analysisDate(LocalDateTime.now())
                .build();
        
        // Анализируем схожесть с другими файлами
        List<SimilarFileDto> similarFileDtos = new ArrayList<>();
        
        for (FileMetadata file : allFiles) {
            // Пропускаем текущий файл
            if (file.getFileId().equals(fileId)) {
                continue;
            }
            
            // Получаем содержимое файла для сравнения
            String otherFileContent = fileStorageClient.getFileContent(file.getFileId()).block();
            
            if (otherFileContent == null) {
                continue;
            }
            
            // Вычисляем схожесть
            double similarity = calculateSimilarity(currentFileContent, otherFileContent);
            
            // Если схожесть выше порога (например, 50%), добавляем в результат
            if (similarity > 50.0) {
                SimilarFile similarFile = SimilarFile.builder()
                        .similarityResult(similarityResult)
                        .fileId(file.getFileId())
                        .fileName(file.getFileName())
                        .similarityScore(similarity)
                        .build();
                
                similarityResult.getSimilarFiles().add(similarFile);
                
                similarFileDtos.add(SimilarFileDto.builder()
                        .fileId(file.getFileId())
                        .fileName(file.getFileName())
                        .similarityScore(similarity)
                        .build());
            }
        }
        
        // Сохраняем результат в БД
        similarityResultRepository.save(similarityResult);
        
        // Возвращаем DTO
        return SimilarityResultDto.builder()
                .fileId(fileId)
                .similarFiles(similarFileDtos)
                .analysisDate(similarityResult.getAnalysisDate())
                .build();
    }

    @Override
    public Resource generateWordCloud(String fileId, String format, Integer width, Integer height) {
        // Получаем содержимое файла
        String content = fileStorageClient.getFileContent(fileId).block();
        
        if (content == null) {
            throw new FileServiceException("Не удалось получить содержимое файла с id: " + fileId);
        }
        
        // Формируем параметры запроса к API облака слов
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("text", content);
        params.put("format", format != null ? format : "svg");
        if (width != null) params.put("width", width);
        if (height != null) params.put("height", height);
        
        // Вызываем внешнее API для генерации облака слов
        byte[] wordCloudImage = webClient.post()
                .uri(wordCloudApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        
        if (wordCloudImage == null) {
            throw new FileServiceException("Не удалось сгенерировать облако слов");
        }
        
        return new ByteArrayResource(wordCloudImage);
    }

    @Override
    public TextStatisticsDto getTextStatistics(String fileId) {
        TextStatistics stats = textStatisticsRepository.findById(fileId)
                .orElseThrow(() -> new FileServiceException("Статистика для файла с id: " + fileId + " не найдена"));
        
        return TextStatisticsDto.builder()
                .fileId(stats.getFileId())
                .paragraphs(stats.getParagraphs())
                .words(stats.getWords())
                .characters(stats.getCharacters())
                .analysisDate(stats.getAnalysisDate())
                .build();
    }

    @Override
    public SimilarityResultDto getSimilarityResult(String fileId) {
        SimilarityResult result = similarityResultRepository.findById(fileId)
                .orElseThrow(() -> new FileServiceException("Результаты анализа схожести для файла с id: " + fileId + " не найдены"));
        
        List<SimilarFileDto> similarFileDtos = result.getSimilarFiles().stream()
                .map(file -> SimilarFileDto.builder()
                        .fileId(file.getFileId())
                        .fileName(file.getFileName())
                        .similarityScore(file.getSimilarityScore())
                        .build())
                .collect(Collectors.toList());
        
        return SimilarityResultDto.builder()
                .fileId(result.getFileId())
                .similarFiles(similarFileDtos)
                .analysisDate(result.getAnalysisDate())
                .build();
    }
    
    // Вспомогательные методы
    
    private int countParagraphs(String text) {
        // Разделяем текст на абзацы по пустым строкам
        String[] paragraphs = text.split("\\n\\s*\\n");
        return paragraphs.length;
    }
    
    private int countWords(String text) {
        // Разделяем текст на слова
        String[] words = text.split("\\s+");
        return words.length;
    }
    
    private double calculateSimilarity(String text1, String text2) {
        // Простой алгоритм для вычисления схожести текстов
        // Разбиваем тексты на слова
        List<String> words1 = Arrays.asList(text1.toLowerCase().split("\\s+"));
        List<String> words2 = Arrays.asList(text2.toLowerCase().split("\\s+"));
        
        // Находим общие слова
        int commonWords = 0;
        for (String word : words1) {
            if (words2.contains(word)) {
                commonWords++;
            }
        }
        
        // Вычисляем схожесть как процент общих слов от общего количества уникальных слов
        int totalUniqueWords = (int) words1.stream().distinct().count() + (int) words2.stream().distinct().count() - commonWords;
        if (totalUniqueWords == 0) {
            return 0.0;
        }
        
        return (commonWords * 100.0) / totalUniqueWords;
    }
} 