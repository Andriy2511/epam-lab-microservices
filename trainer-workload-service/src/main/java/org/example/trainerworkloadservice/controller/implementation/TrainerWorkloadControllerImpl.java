package org.example.trainerworkloadservice.controller.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.controller.TrainerWorkloadController;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer-workload")
@Slf4j
public class TrainerWorkloadControllerImpl implements TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;
    private final TrainingMonthSummaryService trainingMonthSummaryService;

    @Autowired
    public TrainerWorkloadControllerImpl(TrainerWorkloadService trainerWorkloadService, TrainingMonthSummaryService trainingMonthSummaryService) {
        this.trainerWorkloadService = trainerWorkloadService;
        this.trainingMonthSummaryService = trainingMonthSummaryService;
    }

    @Override
    @PatchMapping("/training-time")
    public void updateTimeForTrainer(@RequestBody TrainerWorkloadRequestDTO trainerWorkloadRequestDTO){
        trainerWorkloadService.updateTimeForTrainerWorkload(trainerWorkloadRequestDTO);
    }

    @Override
    @GetMapping("/{trainerId}")
    public TrainingMonthSummary getMonthSummaryByTrainerAndYearAndMonth(@PathVariable Long trainerId, @RequestParam("training-year") int trainingYear, @RequestParam("month-number") int monthNumber){
        return trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(trainerId, trainingYear, monthNumber);
    }
}
