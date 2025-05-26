package ru.hse.fileanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.hse.fileanalysis.model.SimilarityResult;

@Repository
public interface SimilarityResultRepository extends JpaRepository<SimilarityResult, String> {
} 