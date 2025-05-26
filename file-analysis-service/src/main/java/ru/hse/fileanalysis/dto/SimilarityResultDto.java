package ru.hse.fileanalysis.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimilarityResultDto {
    private String fileId;
    private List<SimilarFileDto> similarFiles;
    private LocalDateTime analysisDate;
    
    // Конструкторы
    public SimilarityResultDto() {
    }
    
    public SimilarityResultDto(String fileId, List<SimilarFileDto> similarFiles, LocalDateTime analysisDate) {
        this.fileId = fileId;
        this.similarFiles = similarFiles;
        this.analysisDate = analysisDate;
    }
    
    // Геттеры и сеттеры
    public String getFileId() {
        return fileId;
    }
    
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    public List<SimilarFileDto> getSimilarFiles() {
        return similarFiles;
    }
    
    public void setSimilarFiles(List<SimilarFileDto> similarFiles) {
        this.similarFiles = similarFiles;
    }
    
    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }
    
    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
    
    // Статический класс билдера
    public static SimilarityResultDtoBuilder builder() {
        return new SimilarityResultDtoBuilder();
    }
    
    public static class SimilarityResultDtoBuilder {
        private String fileId;
        private List<SimilarFileDto> similarFiles;
        private LocalDateTime analysisDate;
        
        SimilarityResultDtoBuilder() {
        }
        
        public SimilarityResultDtoBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public SimilarityResultDtoBuilder similarFiles(List<SimilarFileDto> similarFiles) {
            this.similarFiles = similarFiles;
            return this;
        }
        
        public SimilarityResultDtoBuilder analysisDate(LocalDateTime analysisDate) {
            this.analysisDate = analysisDate;
            return this;
        }
        
        public SimilarityResultDto build() {
            return new SimilarityResultDto(fileId, similarFiles, analysisDate);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarityResultDto that = (SimilarityResultDto) o;
        return Objects.equals(fileId, that.fileId) &&
               Objects.equals(similarFiles, that.similarFiles) &&
               Objects.equals(analysisDate, that.analysisDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileId, similarFiles, analysisDate);
    }
} 