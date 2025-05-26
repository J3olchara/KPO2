package ru.hse.filestoring.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class FileEntityTest {

    @Test
    public void testFileEntityBuilder() {
        // Arrange
        String id = "test-id";
        String fileName = "test.txt";
        String contentType = "text/plain";
        Long size = 100L;
        String location = "test-location";
        LocalDateTime uploadDate = LocalDateTime.now();

        // Act
        FileEntity fileEntity = FileEntity.builder()
                .id(id)
                .fileName(fileName)
                .contentType(contentType)
                .size(size)
                .location(location)
                .uploadDate(uploadDate)
                .build();

        // Assert
        assertNotNull(fileEntity);
        assertEquals(id, fileEntity.getId());
        assertEquals(fileName, fileEntity.getFileName());
        assertEquals(contentType, fileEntity.getContentType());
        assertEquals(size, fileEntity.getSize());
        assertEquals(location, fileEntity.getLocation());
        assertEquals(uploadDate, fileEntity.getUploadDate());
    }

    @Test
    public void testFileEntitySettersAndGetters() {
        // Arrange
        FileEntity fileEntity = new FileEntity();
        String id = "test-id";
        String fileName = "test.txt";
        String contentType = "text/plain";
        Long size = 100L;
        String location = "test-location";
        LocalDateTime uploadDate = LocalDateTime.now();

        // Act
        fileEntity.setId(id);
        fileEntity.setFileName(fileName);
        fileEntity.setContentType(contentType);
        fileEntity.setSize(size);
        fileEntity.setLocation(location);
        fileEntity.setUploadDate(uploadDate);

        // Assert
        assertEquals(id, fileEntity.getId());
        assertEquals(fileName, fileEntity.getFileName());
        assertEquals(contentType, fileEntity.getContentType());
        assertEquals(size, fileEntity.getSize());
        assertEquals(location, fileEntity.getLocation());
        assertEquals(uploadDate, fileEntity.getUploadDate());
    }

    @Test
    public void testFileEntityEqualsAndHashCode() {
        // Arrange
        String id = "test-id";
        String fileName = "test.txt";
        String contentType = "text/plain";
        Long size = 100L;
        String location = "test-location";
        LocalDateTime uploadDate = LocalDateTime.now();

        FileEntity fileEntity1 = FileEntity.builder()
                .id(id)
                .fileName(fileName)
                .contentType(contentType)
                .size(size)
                .location(location)
                .uploadDate(uploadDate)
                .build();

        FileEntity fileEntity2 = FileEntity.builder()
                .id(id)
                .fileName(fileName)
                .contentType(contentType)
                .size(size)
                .location(location)
                .uploadDate(uploadDate)
                .build();

        // Assert
        assertEquals(fileEntity1, fileEntity2);
        assertEquals(fileEntity1.hashCode(), fileEntity2.hashCode());
    }
} 