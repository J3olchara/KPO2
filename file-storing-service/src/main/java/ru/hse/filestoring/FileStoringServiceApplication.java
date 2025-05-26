package ru.hse.filestoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.hse.filestoring.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class FileStoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStoringServiceApplication.class, args);
    }
} 