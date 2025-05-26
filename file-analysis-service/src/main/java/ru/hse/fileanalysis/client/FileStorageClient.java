package ru.hse.fileanalysis.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.hse.fileanalysis.exception.FileServiceException;

@Component
public class FileStorageClient {

    private final WebClient webClient;
    
    @Autowired
    public FileStorageClient(WebClient.Builder webClientBuilder, 
                            @Value("${file.storing.service.url}") String fileStoringServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(fileStoringServiceUrl).build();
    }
    
    public Mono<String> getFileContent(String fileId) {
        return webClient.get()
                .uri("/{fileId}", fileId)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(e -> new FileServiceException("Ошибка при получении содержимого файла: " + e.getMessage(), e));
    }
    
    public Mono<FileMetadata[]> getAllFiles() {
        return webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(FileMetadata[].class)
                .onErrorMap(e -> new FileServiceException("Ошибка при получении списка файлов: " + e.getMessage(), e));
    }
} 