package ru.hse.filestoring.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import ru.hse.filestoring.dto.FileUploadResponse;
import ru.hse.filestoring.model.FileEntity;

public interface FileStorageService {
    FileUploadResponse storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileId);
    List<FileEntity> getAllFiles();
    void deleteFile(String fileId);
} 