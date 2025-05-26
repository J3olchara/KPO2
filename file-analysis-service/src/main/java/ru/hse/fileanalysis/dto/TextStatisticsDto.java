package ru.hse.fileanalysis.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class TextStatisticsDto {
    private String fileId;
    private Integer paragraphs;
    private Integer words;
    private Integer characters;
    private LocalDateTime analysisDate;
    
    // Конструкторы
    public TextStatisticsDto() {
    }
    
    public TextStatisticsDto(String fileId, Integer paragraphs, Integer words, Integer characters, LocalDateTime analysisDate) {
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
    public static TextStatisticsDtoBuilder builder() {
        return new TextStatisticsDtoBuilder();
    }
    
    public static class TextStatisticsDtoBuilder {
        private String fileId;
        private Integer paragraphs;
        private Integer words;
        private Integer characters;
        private LocalDateTime analysisDate;
        
        TextStatisticsDtoBuilder() {
        }
        
        public TextStatisticsDtoBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public TextStatisticsDtoBuilder paragraphs(Integer paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        
        public TextStatisticsDtoBuilder words(Integer words) {
            this.words = words;
            return this;
        }
        
        public TextStatisticsDtoBuilder characters(Integer characters) {
            this.characters = characters;
            return this;
        }
        
        public TextStatisticsDtoBuilder analysisDate(LocalDateTime analysisDate) {
            this.analysisDate = analysisDate;
            return this;
        }
        
        public TextStatisticsDto build() {
            return new TextStatisticsDto(fileId, paragraphs, words, characters, analysisDate);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextStatisticsDto that = (TextStatisticsDto) o;
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