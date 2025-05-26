package ru.hse.fileanalysis.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.hse.fileanalysis.exception.FileServiceException;

public class FileStorageClientTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    
    private FileStorageClient fileStorageClient;
    
    private String fileStoringServiceUrl = "http://file-storing-service:8081/api/files";
    private String fileId;
    private String fileContent;
    private FileMetadata[] fileMetadatas;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setup() {
        fileId = UUID.randomUUID().toString();
        fileContent = "This is a test file content";
        
        LocalDateTime now = LocalDateTime.now();
        fileMetadatas = new FileMetadata[] {
            FileMetadata.builder()
                .fileId(fileId)
                .fileName("test.txt")
                .size(100L)
                .uploadDate(now)
                .build(),
            FileMetadata.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName("test2.txt")
                .size(200L)
                .uploadDate(now)
                .build()
        };
        
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        
        when(webClientBuilder.baseUrl(fileStoringServiceUrl)).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        fileStorageClient = new FileStorageClient(webClientBuilder, fileStoringServiceUrl);
    }
    
    @Test
    public void testGetFileContent_Success() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{fileId}"), eq(fileId))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.TEXT_PLAIN)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(fileContent));
        
        // Act
        Mono<String> result = fileStorageClient.getFileContent(fileId);
        
        // Assert
        StepVerifier.create(result)
            .expectNext(fileContent)
            .verifyComplete();
    }
    
    @Test
    public void testGetFileContent_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/{fileId}"), eq(fileId))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.TEXT_PLAIN)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("Service unavailable")));
        
        // Act
        Mono<String> result = fileStorageClient.getFileContent(fileId);
        
        // Assert
        StepVerifier.create(result)
            .expectError(FileServiceException.class)
            .verify();
    }
    
    @Test
    public void testGetAllFiles_Success() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FileMetadata[].class)).thenReturn(Mono.just(fileMetadatas));
        
        // Act
        Mono<FileMetadata[]> result = fileStorageClient.getAllFiles();
        
        // Assert
        StepVerifier.create(result)
            .assertNext(files -> {
                assertEquals(2, files.length);
                assertEquals(fileId, files[0].getFileId());
                assertEquals("test.txt", files[0].getFileName());
                assertEquals(100L, files[0].getSize());
            })
            .verifyComplete();
    }
    
    @Test
    public void testGetAllFiles_Error() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FileMetadata[].class)).thenReturn(Mono.error(new RuntimeException("Service unavailable")));
        
        // Act
        Mono<FileMetadata[]> result = fileStorageClient.getAllFiles();
        
        // Assert
        StepVerifier.create(result)
            .expectError(FileServiceException.class)
            .verify();
    }
} 