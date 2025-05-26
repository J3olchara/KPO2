package ru.hse.filestoring.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class FileEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String contentType;
    
    @Column(nullable = false)
    private Long size;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private LocalDateTime uploadDate;
    
    // Геттеры и сеттеры
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    // Статический внутренний класс Builder
    public static FileEntityBuilder builder() {
        return new FileEntityBuilder();
    }
    
    public static class FileEntityBuilder {
        private String id;
        private String fileName;
        private String contentType;
        private Long size;
        private String location;
        private LocalDateTime uploadDate;
        
        public FileEntityBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public FileEntityBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public FileEntityBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
        
        public FileEntityBuilder size(Long size) {
            this.size = size;
            return this;
        }
        
        public FileEntityBuilder location(String location) {
            this.location = location;
            return this;
        }
        
        public FileEntityBuilder uploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
            return this;
        }
        
        public FileEntity build() {
            FileEntity fileEntity = new FileEntity();
            fileEntity.id = this.id;
            fileEntity.fileName = this.fileName;
            fileEntity.contentType = this.contentType;
            fileEntity.size = this.size;
            fileEntity.location = this.location;
            fileEntity.uploadDate = this.uploadDate;
            return fileEntity;
        }
    }

    // Методы equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(size, that.size) &&
                Objects.equals(location, that.location) &&
                Objects.equals(uploadDate, that.uploadDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, contentType, size, location, uploadDate);
    }
} 