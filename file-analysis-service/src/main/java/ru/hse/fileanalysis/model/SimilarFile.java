package ru.hse.fileanalysis.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "similar_files")
public class SimilarFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "similarity_result_id", nullable = false)
    private SimilarityResult similarityResult;
    
    @Column(nullable = false)
    private String fileId;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private Double similarityScore; // От 0 до 100
    
    // Конструкторы
    public SimilarFile() {
    }
    
    public SimilarFile(Long id, SimilarityResult similarityResult, String fileId, String fileName, Double similarityScore) {
        this.id = id;
        this.similarityResult = similarityResult;
        this.fileId = fileId;
        this.fileName = fileName;
        this.similarityScore = similarityScore;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public SimilarityResult getSimilarityResult() {
        return similarityResult;
    }
    
    public void setSimilarityResult(SimilarityResult similarityResult) {
        this.similarityResult = similarityResult;
    }
    
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
    public static SimilarFileBuilder builder() {
        return new SimilarFileBuilder();
    }
    
    public static class SimilarFileBuilder {
        private Long id;
        private SimilarityResult similarityResult;
        private String fileId;
        private String fileName;
        private Double similarityScore;
        
        SimilarFileBuilder() {
        }
        
        public SimilarFileBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public SimilarFileBuilder similarityResult(SimilarityResult similarityResult) {
            this.similarityResult = similarityResult;
            return this;
        }
        
        public SimilarFileBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public SimilarFileBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public SimilarFileBuilder similarityScore(Double similarityScore) {
            this.similarityScore = similarityScore;
            return this;
        }
        
        public SimilarFile build() {
            return new SimilarFile(id, similarityResult, fileId, fileName, similarityScore);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarFile that = (SimilarFile) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(similarityResult, that.similarityResult) &&
               Objects.equals(fileId, that.fileId) &&
               Objects.equals(fileName, that.fileName) &&
               Objects.equals(similarityScore, that.similarityScore);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, similarityResult, fileId, fileName, similarityScore);
    }
} 