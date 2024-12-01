package org.example.trainerworkloadservice.service;

import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingYear;

import java.util.Optional;

public interface TrainingYearService {
    Optional<TrainingYear> getByTrainingYearAndTrainerWorkload(int trainingYear, TrainerWorkload trainerWorkload);

    TrainingYear addTrainingYear(TrainingYear trainingYear);
}
