package org.example.trainerworkloadservice.service;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;

import java.util.Optional;

public interface TrainingMonthSummaryService {
    Optional<TrainingMonthSummary> getMonthByMonthNumberAndTrainingYear(int monthNumber, TrainingYear trainingYear);

    TrainingMonthSummary updateTrainingMonthSummary(TrainingMonthSummary trainingMonthSummary);

    TrainingMonthSummary getByTrainerIdAndYearAndMonthNumber(Long trainerId, int year, int monthNumber);
}
