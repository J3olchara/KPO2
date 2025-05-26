package ru.hse.fileanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.hse.fileanalysis.model.TextStatistics;

@Repository
public interface TextStatisticsRepository extends JpaRepository<TextStatistics, String> {
}