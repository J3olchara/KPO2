package ru.hse.filestoring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import ru.hse.filestoring.model.FileEntity;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void testSaveFile() {
        // Arrange
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName("test.txt");
        fileEntity.setContentType("text/plain");
        fileEntity.setSize(100L);
        fileEntity.setLocation("test-location");
        fileEntity.setUploadDate(LocalDateTime.now());

        // Act
        FileEntity savedEntity = fileRepository.save(fileEntity);

        // Assert
        assertTrue(savedEntity.getId() != null);
        assertEquals("test.txt", savedEntity.getFileName());
    }

    @Test
    public void testFindById() {
        // Arrange
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName("test.txt");
        fileEntity.setContentType("text/plain");
        fileEntity.setSize(100L);
        fileEntity.setLocation("test-location");
        fileEntity.setUploadDate(LocalDateTime.now());
        
        FileEntity savedEntity = entityManager.persistAndFlush(fileEntity);
        String fileId = savedEntity.getId();

        // Act
        Optional<FileEntity> foundEntity = fileRepository.findById(fileId);

        // Assert
        assertTrue(foundEntity.isPresent());
        assertEquals(fileId, foundEntity.get().getId());
        assertEquals("test.txt", foundEntity.get().getFileName());
    }

    @Test
    public void testFindAll() {
        // Arrange
        FileEntity fileEntity1 = new FileEntity();
        fileEntity1.setFileName("test1.txt");
        fileEntity1.setContentType("text/plain");
        fileEntity1.setSize(100L);
        fileEntity1.setLocation("test-location-1");
        fileEntity1.setUploadDate(LocalDateTime.now());
        
        FileEntity fileEntity2 = new FileEntity();
        fileEntity2.setFileName("test2.txt");
        fileEntity2.setContentType("text/plain");
        fileEntity2.setSize(200L);
        fileEntity2.setLocation("test-location-2");
        fileEntity2.setUploadDate(LocalDateTime.now());
        
        entityManager.persist(fileEntity1);
        entityManager.persist(fileEntity2);
        entityManager.flush();

        // Act
        List<FileEntity> fileEntities = fileRepository.findAll();

        // Assert
        assertFalse(fileEntities.isEmpty());
        assertTrue(fileEntities.size() >= 2);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName("test.txt");
        fileEntity.setContentType("text/plain");
        fileEntity.setSize(100L);
        fileEntity.setLocation("test-location");
        fileEntity.setUploadDate(LocalDateTime.now());
        
        FileEntity savedEntity = entityManager.persistAndFlush(fileEntity);
        String fileId = savedEntity.getId();

        // Act
        fileRepository.deleteById(fileId);
        Optional<FileEntity> deletedEntity = fileRepository.findById(fileId);

        // Assert
        assertFalse(deletedEntity.isPresent());
    }
} 