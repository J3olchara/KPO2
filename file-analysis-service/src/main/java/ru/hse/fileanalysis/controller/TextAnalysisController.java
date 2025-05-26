package ru.hse.fileanalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.hse.fileanalysis.dto.SimilarityResultDto;
import ru.hse.fileanalysis.dto.TextStatisticsDto;
import ru.hse.fileanalysis.exception.FileServiceException;
import ru.hse.fileanalysis.service.TextAnalysisService;

@RestController
@RequestMapping("/api/analysis")
@Tag(name = "Text Analysis API", description = "API для анализа текстовых файлов")
public class TextAnalysisController {

    @Autowired
    private TextAnalysisService textAnalysisService;
    
    @PostMapping("/stats/{fileId}")
    @Operation(summary = "Анализировать статистику текста", description = "Анализирует статистику текстового файла и возвращает результаты")
    public ResponseEntity<TextStatisticsDto> analyzeTextStatistics(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        TextStatisticsDto statistics = textAnalysisService.analyzeTextStatistics(fileId);
        
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/similarity/{fileId}")
    @Operation(summary = "Анализировать схожесть", description = "Анализирует схожесть файла с другими файлами")
    public ResponseEntity<SimilarityResultDto> analyzeSimilarity(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        SimilarityResultDto similarityResult = textAnalysisService.analyzeSimilarity(fileId);
        
        return ResponseEntity.ok(similarityResult);
    }

    @GetMapping("/wordcloud/{fileId}")
    @Operation(summary = "Получить облако слов", description = "Генерирует и возвращает облако слов для файла")
    public ResponseEntity<Resource> getWordCloud(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId,
            @Parameter(description = "Формат изображения (svg/png)") 
            @RequestParam(required = false, defaultValue = "svg") String format,
            @Parameter(description = "Ширина изображения") 
            @RequestParam(required = false) Integer width,
            @Parameter(description = "Высота изображения") 
            @RequestParam(required = false) Integer height) {
        
        Resource wordCloud = textAnalysisService.generateWordCloud(fileId, format, width, height);
        
        MediaType mediaType = format.equalsIgnoreCase("svg") 
                ? MediaType.valueOf("image/svg+xml") 
                : MediaType.IMAGE_PNG;
        
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"wordcloud." + format + "\"")
                .body(wordCloud);
    }

    @GetMapping("/stats/{fileId}")
    @Operation(summary = "Получить статистику текста", description = "Получает сохраненную статистику текстового файла")
    public ResponseEntity<TextStatisticsDto> getTextStatistics(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        TextStatisticsDto statistics = textAnalysisService.getTextStatistics(fileId);
        
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/similarity/{fileId}")
    @Operation(summary = "Получить результаты анализа схожести", description = "Получает сохраненные результаты анализа схожести файла")
    public ResponseEntity<SimilarityResultDto> getSimilarityResult(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        SimilarityResultDto similarityResult = textAnalysisService.getSimilarityResult(fileId);
        
        return ResponseEntity.ok(similarityResult);
    }
    
    @ExceptionHandler(FileServiceException.class)
    public ResponseEntity<String> handleFileServiceException(FileServiceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
} 