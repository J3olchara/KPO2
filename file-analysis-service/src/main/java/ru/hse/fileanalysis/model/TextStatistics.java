package ru.hse.fileanalysis.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "text_statistics")
public class TextStatistics {
    
    @Id
    private String fileId;
    
    @Column(nullable = false)
    private Integer paragraphs;
    
    @Column(nullable = false)
    private Integer words;
    
    @Column(nullable = false)
    private Integer characters;
    
    @Column(nullable = false)
    private LocalDateTime analysisDate;
    
    // Конструкторы
    public TextStatistics() {
    }
    
    public TextStatistics(String fileId, Integer paragraphs, Integer words, Integer characters, LocalDateTime analysisDate) {
        this.fileId = fileId;
        this.paragraphs = paragraphs;
        this.words = words;
        this.characters = characters;
        this.analysisDate = analysisDate;
    }
    
    // Геттеры и сеттеры
    public String getFileId() {
        return fileId;
    }
    
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    public Integer getParagraphs() {
        return paragraphs;
    }
    
    public void setParagraphs(Integer paragraphs) {
        this.paragraphs = paragraphs;
    }
    
    public Integer getWords() {
        return words;
    }
    
    public void setWords(Integer words) {
        this.words = words;
    }
    
    public Integer getCharacters() {
        return characters;
    }
    
    public void setCharacters(Integer characters) {
        this.characters = characters;
    }
    
    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }
    
    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
    
    // Builder паттерн
    public static TextStatisticsBuilder builder() {
        return new TextStatisticsBuilder();
    }
    
    public static class TextStatisticsBuilder {
        private String fileId;
        private Integer paragraphs;
        private Integer words;
        private Integer characters;
        private LocalDateTime analysisDate;
        
        TextStatisticsBuilder() {
        }
        
        public TextStatisticsBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public TextStatisticsBuilder paragraphs(Integer paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        
        public TextStatisticsBuilder words(Integer words) {
            this.words = words;
            return this;
        }
        
        public TextStatisticsBuilder characters(Integer characters) {
            this.characters = characters;
            return this;
        }
        
        public TextStatisticsBuilder analysisDate(LocalDateTime analysisDate) {
            this.analysisDate = analysisDate;
            return this;
        }
        
        public TextStatistics build() {
            return new TextStatistics(fileId, paragraphs, words, characters, analysisDate);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextStatistics that = (TextStatistics) o;
        return Objects.equals(fileId, that.fileId) &&
               Objects.equals(paragraphs, that.paragraphs) &&
               Objects.equals(words, that.words) &&
               Objects.equals(characters, that.characters) &&
               Objects.equals(analysisDate, that.analysisDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileId, paragraphs, words, characters, analysisDate);
    }
} 