package ru.hse.fileanalysis.client;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileMetadata {
    private String fileId;
    private String fileName;
    private Long size;
    private LocalDateTime uploadDate;
    
    // Конструкторы
    public FileMetadata() {
    }
    
    public FileMetadata(String fileId, String fileName, Long size, LocalDateTime uploadDate) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.size = size;
        this.uploadDate = uploadDate;
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
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    // Builder паттерн
    public static FileMetadataBuilder builder() {
        return new FileMetadataBuilder();
    }
    
    public static class FileMetadataBuilder {
        private String fileId;
        private String fileName;
        private Long size;
        private LocalDateTime uploadDate;
        
        FileMetadataBuilder() {
        }
        
        public FileMetadataBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public FileMetadataBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public FileMetadataBuilder size(Long size) {
            this.size = size;
            return this;
        }
        
        public FileMetadataBuilder uploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
            return this;
        }
        
        public FileMetadata build() {
            return new FileMetadata(fileId, fileName, size, uploadDate);
        }
    }
    
    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(fileId, that.fileId) &&
               Objects.equals(fileName, that.fileName) &&
               Objects.equals(size, that.size) &&
               Objects.equals(uploadDate, that.uploadDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileName, size, uploadDate);
    }
} 