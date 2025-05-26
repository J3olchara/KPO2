package ru.hse.filestoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.hse.filestoring.model.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
} 