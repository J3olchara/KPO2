package ru.hse.fileanalysis.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "similarity_results")
public class SimilarityResult {
    
    @Id
    private String fileId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "similarityResult")
    private List<SimilarFile> similarFiles;
    
    @Column(nullable = false)
    private LocalDateTime analysisDate;
    
    // Конструкторы
    public SimilarityResult() {
    }
    
    public SimilarityResult(String fileId, List<SimilarFile> similarFiles, LocalDateTime analysisDate) {
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
    
    public List<SimilarFile> getSimilarFiles() {
        return similarFiles;
    }
    
    public void setSimilarFiles(List<SimilarFile> similarFiles) {
        this.similarFiles = similarFiles;
    }
    
    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }
    
    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
    
    // Builder паттерн
    public static SimilarityResultBuilder builder() {
        return new SimilarityResultBuilder();
    }
    
    public static class SimilarityResultBuilder {
        private String fileId;
        private List<SimilarFile> similarFiles;
        private LocalDateTime analysisDate;
        
        SimilarityResultBuilder() {
        }
        
        public SimilarityResultBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public SimilarityResultBuilder similarFiles(List<SimilarFile> similarFiles) {
            this.similarFiles = similarFiles;
            return this;
        }
        
        public SimilarityResultBuilder analysisDate(LocalDateTime analysisDate) {
            this.analysisDate = analysisDate;
            return this;
        }
        
        public SimilarityResult build() {
            return new SimilarityResult(fileId, similarFiles, analysisDate);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarityResult that = (SimilarityResult) o;
        return Objects.equals(fileId, that.fileId) &&
               Objects.equals(similarFiles, that.similarFiles) &&
               Objects.equals(analysisDate, that.analysisDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileId, similarFiles, analysisDate);
    }
} 