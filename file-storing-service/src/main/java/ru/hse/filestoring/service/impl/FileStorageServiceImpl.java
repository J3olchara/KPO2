package ru.hse.filestoring.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ru.hse.filestoring.config.FileStorageProperties;
import ru.hse.filestoring.dto.FileUploadResponse;
import ru.hse.filestoring.exception.FileNotFoundException;
import ru.hse.filestoring.exception.FileStorageException;
import ru.hse.filestoring.model.FileEntity;
import ru.hse.filestoring.repository.FileRepository;
import ru.hse.filestoring.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    private final FileRepository fileRepository;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties, FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Не удалось создать директорию для хранения файлов", ex);
        }
    }

    @Override
    public FileUploadResponse storeFile(MultipartFile file) {
        // Проверка имени файла
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) {
            throw new FileStorageException("Имя файла содержит недопустимую последовательность символов " + originalFileName);
        }

        try {
            // Проверка содержимого файла
            if (file.isEmpty()) {
                throw new FileStorageException("Файл пуст " + originalFileName);
            }

            // Генерация уникального ID для файла
            String fileId = UUID.randomUUID().toString();
            
            // Получаем расширение файла
            String fileExtension = "";
            int lastDotIndex = originalFileName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                fileExtension = originalFileName.substring(lastDotIndex);
            }
            
            // Сохраняем файл с уникальным именем
            String storedFileName = fileId + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Сохраняем метаданные файла в БД
            FileEntity fileEntity = FileEntity.builder()
                    .id(fileId)
                    .fileName(originalFileName)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .location(storedFileName)
                    .uploadDate(LocalDateTime.now())
                    .build();
            
            fileRepository.save(fileEntity);
            
            // Возвращаем ответ
            return FileUploadResponse.builder()
                    .fileId(fileEntity.getId())
                    .fileName(fileEntity.getFileName())
                    .size(fileEntity.getSize())
                    .uploadDate(fileEntity.getUploadDate())
                    .build();
                    
        } catch (IOException ex) {
            throw new FileStorageException("Не удалось сохранить файл " + originalFileName, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileId) {
        try {
            FileEntity fileEntity = fileRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("Файл не найден с id: " + fileId));
            
            Path filePath = this.fileStorageLocation.resolve(fileEntity.getLocation()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Файл не найден: " + fileEntity.getLocation());
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Файл не найден с id: " + fileId, ex);
        }
    }

    @Override
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public void deleteFile(String fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Файл не найден с id: " + fileId));

        try {
            Path filePath = this.fileStorageLocation.resolve(fileEntity.getLocation()).normalize();
            Files.deleteIfExists(filePath);
            fileRepository.delete(fileEntity);
        } catch (IOException ex) {
            throw new FileStorageException("Не удалось удалить файл: " + fileEntity.getLocation(), ex);
        }
    }
} 