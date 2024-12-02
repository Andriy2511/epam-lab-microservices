package org.example.trainerworkloadservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface TrainerWorkloadController {
    @Operation(summary = "Updates trainer workload",
            description = "The method updates trainee profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer workload updated successfully"),
            @ApiResponse(responseCode = "400", description = "An error arisen during the method runtime")
    })
    void updateTimeForTrainer(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO);

    @Operation(summary = "Receives trainer workload by id, year, and month",
            description = "The method updates trainee profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Month summary found successfully"),
            @ApiResponse(responseCode = "400", description = "Month summary not found")
    })
    TrainingMonthSummary getMonthSummaryByTrainerAndYearAndMonth(@PathVariable Long trainerId, @RequestParam("training-year") int trainingYear, @RequestParam("month-number") int monthNumber);
}
