package ru.hse.filestoring.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.hse.filestoring.dto.FileUploadResponse;
import ru.hse.filestoring.model.FileEntity;
import ru.hse.filestoring.service.FileStorageService;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File API", description = "API для работы с файлами")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/upload")
    @Operation(summary = "Загрузить файл", description = "Загружает текстовый файл и возвращает его идентификатор")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "Файл для загрузки (только .txt)") 
            @RequestParam("file") MultipartFile file) {
        
        FileUploadResponse response = fileStorageService.storeFile(file);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Получить файл", description = "Возвращает файл по его идентификатору")
    public ResponseEntity<Resource> getFile(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        Resource resource = fileStorageService.loadFileAsResource(fileId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping
    @Operation(summary = "Получить список всех файлов", description = "Возвращает список всех загруженных файлов")
    public ResponseEntity<List<FileUploadResponse>> getAllFiles() {
        List<FileUploadResponse> files = fileStorageService.getAllFiles().stream()
                .map(file -> FileUploadResponse.builder()
                        .fileId(file.getId())
                        .fileName(file.getFileName())
                        .size(file.getSize())
                        .uploadDate(file.getUploadDate())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Удалить файл", description = "Удаляет файл по его идентификатору")
    public ResponseEntity<Object> deleteFile(
            @Parameter(description = "Идентификатор файла") 
            @PathVariable String fileId) {
        
        fileStorageService.deleteFile(fileId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"success\": true, \"message\": \"Файл успешно удален\"}");
    }
} 