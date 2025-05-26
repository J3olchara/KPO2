package ru.hse.filestoring.dto;

import java.time.LocalDateTime;

public class FileUploadResponse {
    private String fileId;
    private String fileName;
    private Long size;
    private LocalDateTime uploadDate;
    
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
    
    // Статический внутренний класс Builder
    public static FileUploadResponseBuilder builder() {
        return new FileUploadResponseBuilder();
    }
    
    public static class FileUploadResponseBuilder {
        private String fileId;
        private String fileName;
        private Long size;
        private LocalDateTime uploadDate;
        
        public FileUploadResponseBuilder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }
        
        public FileUploadResponseBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public FileUploadResponseBuilder size(Long size) {
            this.size = size;
            return this;
        }
        
        public FileUploadResponseBuilder uploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
            return this;
        }
        
        public FileUploadResponse build() {
            FileUploadResponse response = new FileUploadResponse();
            response.fileId = this.fileId;
            response.fileName = this.fileName;
            response.size = this.size;
            response.uploadDate = this.uploadDate;
            return response;
        }
    }
} 