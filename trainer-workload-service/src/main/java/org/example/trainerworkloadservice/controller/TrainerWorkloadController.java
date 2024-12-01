package org.example.trainerworkloadservice.controller;

import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface TrainerWorkloadController {
    void updateTimeForTrainer(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO);

    @GetMapping("/{trainerId}")
    TrainingMonthSummary getMonthSummaryByTrainerAndYearAndMonth(@PathVariable Long trainerId, @RequestParam("training-year") int trainingYear, @RequestParam("month-number") int monthNumber);
}
