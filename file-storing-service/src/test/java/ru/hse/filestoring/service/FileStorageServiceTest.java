package ru.hse.filestoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import ru.hse.filestoring.config.FileStorageProperties;
import ru.hse.filestoring.dto.FileUploadResponse;
import ru.hse.filestoring.exception.FileNotFoundException;
import ru.hse.filestoring.exception.FileStorageException;
import ru.hse.filestoring.model.FileEntity;
import ru.hse.filestoring.repository.FileRepository;
import ru.hse.filestoring.service.impl.FileStorageServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileStorageProperties fileStorageProperties;

    private FileStorageService fileStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setup() {
        when(fileStorageProperties.getLocation()).thenReturn(tempDir.toString());
        fileStorageService = new FileStorageServiceImpl(fileStorageProperties, fileRepository);
    }

    @Test
    public void testStoreFile_Success() {
        // Arrange
        String content = "Test file content";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                content.getBytes());

        String fileId = UUID.randomUUID().toString();
        FileEntity savedEntity = new FileEntity();
        savedEntity.setId(fileId);
        savedEntity.setFileName("test.txt");
        savedEntity.setContentType("text/plain");
        savedEntity.setSize((long) content.length());
        savedEntity.setLocation(fileId + ".txt");
        savedEntity.setUploadDate(LocalDateTime.now());

        when(fileRepository.save(any(FileEntity.class))).thenReturn(savedEntity);

        // Act
        FileUploadResponse response = fileStorageService.storeFile(file);

        // Assert
        assertNotNull(response);
        assertEquals("test.txt", response.getFileName());
        assertEquals((long) content.length(), response.getSize());

        // Verify that repository was called
        ArgumentCaptor<FileEntity> entityCaptor = ArgumentCaptor.forClass(FileEntity.class);
        verify(fileRepository, times(1)).save(entityCaptor.capture());
        
        // Verify that file exists in storage
        FileEntity capturedEntity = entityCaptor.getValue();
        assertEquals("test.txt", capturedEntity.getFileName());
        assertEquals("text/plain", capturedEntity.getContentType());
        assertEquals((long) content.length(), capturedEntity.getSize());
    }

    @Test
    public void testStoreFile_WithInvalidFileName() {
        // Arrange
        String content = "Test file content";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "../invalid/path/test.txt",
                "text/plain",
                content.getBytes());

        // Act & Assert
        assertThrows(FileStorageException.class, () -> fileStorageService.storeFile(file));
    }

    @Test
    public void testStoreFile_WithEmptyFile() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]);

        // Act & Assert
        assertThrows(FileStorageException.class, () -> fileStorageService.storeFile(file));
    }

    @Test
    public void testLoadFileAsResource_Success() throws IOException {
        // Arrange
        String fileId = UUID.randomUUID().toString();
        String content = "Test file content";
        
        // Create a test file
        Path filePath = tempDir.resolve(fileId + ".txt");
        Files.write(filePath, content.getBytes());
        
        FileEntity fileEntity = FileEntity.builder()
                .id(fileId)
                .fileName("test.txt")
                .contentType("text/plain")
                .size((long) content.length())
                .location(fileId + ".txt")
                .uploadDate(LocalDateTime.now())
                .build();
        
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));

        // Act
        Resource resource = fileStorageService.loadFileAsResource(fileId);

        // Assert
        assertNotNull(resource);
        assertEquals(content, new String(resource.getInputStream().readAllBytes()));
    }

    @Test
    public void testLoadFileAsResource_FileNotFound() {
        // Arrange
        String fileId = UUID.randomUUID().toString();
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> fileStorageService.loadFileAsResource(fileId));
    }

    @Test
    public void testGetAllFiles() {
        // Arrange
        List<FileEntity> files = Arrays.asList(
                FileEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .fileName("file1.txt")
                        .contentType("text/plain")
                        .size(100L)
                        .location("file1.txt")
                        .uploadDate(LocalDateTime.now())
                        .build(),
                FileEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .fileName("file2.txt")
                        .contentType("text/plain")
                        .size(200L)
                        .location("file2.txt")
                        .uploadDate(LocalDateTime.now())
                        .build()
        );
        
        when(fileRepository.findAll()).thenReturn(files);

        // Act
        List<FileEntity> result = fileStorageService.getAllFiles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getFileName());
        assertEquals("file2.txt", result.get(1).getFileName());
    }

    @Test
    public void testDeleteFile_Success() throws IOException {
        // Arrange
        String fileId = UUID.randomUUID().toString();
        String content = "Test file content";
        
        // Create a test file
        Path filePath = tempDir.resolve(fileId + ".txt");
        Files.write(filePath, content.getBytes());
        
        FileEntity fileEntity = FileEntity.builder()
                .id(fileId)
                .fileName("test.txt")
                .contentType("text/plain")
                .size((long) content.length())
                .location(fileId + ".txt")
                .uploadDate(LocalDateTime.now())
                .build();
        
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));

        // Act
        fileStorageService.deleteFile(fileId);

        // Assert
        verify(fileRepository, times(1)).delete(fileEntity);
    }

    @Test
    public void testDeleteFile_FileNotFound() {
        // Arrange
        String fileId = UUID.randomUUID().toString();
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> fileStorageService.deleteFile(fileId));
    }
} 