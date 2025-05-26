package ru.hse.filestoring.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import ru.hse.filestoring.dto.FileUploadResponse;
import ru.hse.filestoring.exception.FileNotFoundException;
import ru.hse.filestoring.model.FileEntity;
import ru.hse.filestoring.service.FileStorageService;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    private String fileId;
    private FileUploadResponse fileUploadResponse;
    private List<FileEntity> fileEntities;

    @BeforeEach
    public void setup() {
        fileId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        fileUploadResponse = FileUploadResponse.builder()
                .fileId(fileId)
                .fileName("test.txt")
                .size(100L)
                .uploadDate(now)
                .build();
        
        fileEntities = Arrays.asList(
                FileEntity.builder()
                        .id(fileId)
                        .fileName("test.txt")
                        .contentType("text/plain")
                        .size(100L)
                        .location(fileId + ".txt")
                        .uploadDate(now)
                        .build(),
                FileEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .fileName("test2.txt")
                        .contentType("text/plain")
                        .size(200L)
                        .location("test2.txt")
                        .uploadDate(now)
                        .build()
        );
    }

    @Test
    public void testUploadFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test file content".getBytes());

        when(fileStorageService.storeFile(any())).thenReturn(fileUploadResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId", is(fileId)))
                .andExpect(jsonPath("$.fileName", is("test.txt")))
                .andExpect(jsonPath("$.size", is(100)));
        
        verify(fileStorageService, times(1)).storeFile(any());
    }

    @Test
    public void testGetFile() throws Exception {
        // Arrange
        String content = "Test file content";
        Resource resource = new ByteArrayResource(content.getBytes());
        when(fileStorageService.loadFileAsResource(fileId)).thenReturn(resource);

        // Act & Assert
        mockMvc.perform(get("/api/files/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().string(content));
        
        verify(fileStorageService, times(1)).loadFileAsResource(fileId);
    }

    @Test
    public void testGetFile_NotFound() throws Exception {
        // Arrange
        when(fileStorageService.loadFileAsResource(fileId))
                .thenThrow(new FileNotFoundException("File not found: " + fileId));

        // Act & Assert
        mockMvc.perform(get("/api/files/{fileId}", fileId))
                .andExpect(status().isNotFound());
        
        verify(fileStorageService, times(1)).loadFileAsResource(fileId);
    }

    @Test
    public void testGetAllFiles() throws Exception {
        // Arrange
        when(fileStorageService.getAllFiles()).thenReturn(fileEntities);

        // Act & Assert
        mockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fileId", is(fileId)))
                .andExpect(jsonPath("$[0].fileName", is("test.txt")))
                .andExpect(jsonPath("$[0].size", is(100)))
                .andExpect(jsonPath("$[1].fileName", is("test2.txt")))
                .andExpect(jsonPath("$[1].size", is(200)));
        
        verify(fileStorageService, times(1)).getAllFiles();
    }

    @Test
    public void testDeleteFile() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/files/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Файл успешно удален")));
        
        verify(fileStorageService, times(1)).deleteFile(fileId);
    }

    @Test
    public void testDeleteFile_NotFound() throws Exception {
        // Arrange
        doThrow(new FileNotFoundException("File not found: " + fileId))
                .when(fileStorageService).deleteFile(fileId);

        // Act & Assert
        mockMvc.perform(delete("/api/files/{fileId}", fileId))
                .andExpect(status().isNotFound());
        
        verify(fileStorageService, times(1)).deleteFile(fileId);
    }
} 