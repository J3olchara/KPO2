package ru.hse.fileanalysis.service;

import java.util.List;

import org.springframework.core.io.Resource;

import ru.hse.fileanalysis.dto.SimilarityResultDto;
import ru.hse.fileanalysis.dto.TextStatisticsDto;

public interface TextAnalysisService {
    TextStatisticsDto analyzeTextStatistics(String fileId);
    SimilarityResultDto analyzeSimilarity(String fileId);
    Resource generateWordCloud(String fileId, String format, Integer width, Integer height);
    TextStatisticsDto getTextStatistics(String fileId);
    SimilarityResultDto getSimilarityResult(String fileId);
} 