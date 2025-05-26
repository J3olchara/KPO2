package ru.hse.fileanalysis.dto;

import java.util.Objects;

public class SimilarFileDto {
    private String fileId;
    private String fileName;
    private Double similarityScore;
    
    // Конструкторы
    public SimilarFileDto() {
    }
    
    public SimilarFileDto(String fileId, String fileName, Double similarityScore) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.similarityScore = similarityScore;
    }
    
    // Геттеры и сеттеры
    public String getFileId() {
        return fileId;
    }
    
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Double getSimilarityScore() {
        return similarityScore;
    }
    
    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }
    
    // Builder паттерн
    public static SimilarFileDtoBuilder builder() {
        return new SimilarFileDtoBuilder();
    }
    
    public static class SimilarFileDtoBuilder {
        private String fileId;
        private String fileName;
        private Double similarityScore;
        
        SimilarFileDtoBuilder() {
        }
        
        public SimilarFileDtoBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public SimilarFileDtoBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public SimilarFileDtoBuilder similarityScore(Double similarityScore) {
            this.similarityScore = similarityScore;
            return this;
        }
        
        public SimilarFileDto build() {
            return new SimilarFileDto(fileId, fileName, similarityScore);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarFileDto that = (SimilarFileDto) o;
        return Objects.equals(fileId, that.fileId) &&
               Objects.equals(fileName, that.fileName) &&
               Objects.equals(similarityScore, that.similarityScore);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileName, similarityScore);
    }
} 