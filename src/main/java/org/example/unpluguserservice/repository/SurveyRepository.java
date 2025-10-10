package org.example.unpluguserservice.repository;

import org.example.unpluguserservice.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyResult, Long> {
    Optional<SurveyResult> findByUsername(String username);
}
