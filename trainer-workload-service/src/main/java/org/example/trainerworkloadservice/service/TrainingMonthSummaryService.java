package org.example.trainerworkloadservice.service;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;

public interface TrainingMonthSummaryService {

    TrainingMonthSummary getByTrainerIdAndYearAndMonthNumber(Long trainerId, int year, int monthNumber);
}
