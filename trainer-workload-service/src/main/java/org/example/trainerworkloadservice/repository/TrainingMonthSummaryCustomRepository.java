package org.example.trainerworkloadservice.repository;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;

import java.util.Optional;

public interface TrainingMonthSummaryCustomRepository {
    Optional<TrainingMonthSummary> findTrainingMonthSummary(Long trainerId, int trainingYear, int monthNumber);
}
