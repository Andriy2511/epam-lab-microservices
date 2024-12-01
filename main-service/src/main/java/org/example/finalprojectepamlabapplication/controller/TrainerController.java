package org.example.finalprojectepamlabapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface TrainerController {

    @Operation(summary = "Receives trainer by ID.",
            description = "The method receives trainer by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    TrainerDTO getTrainer(@PathVariable Long id);

    @Operation(summary = "Updates trainer profile.",
            description = "The method updates trainer profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    TrainerDTO updateTrainer(@PathVariable Long id, TrainerDTO trainerDTO);

    @Operation(summary = "Finds training with criterion",
            description = "The method finds training assigned to the trainer with a specific criterion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    List<TrainingDTO> getTrainingByTrainerWithCriterion(@PathVariable Long id,
                                                                        @RequestParam(name = "to-date", required = false) Date toDate,
                                                                        @RequestParam(name = "from-date", required = false) Date fromDate,
                                                                        @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                                        @RequestParam(name = "trainee-username", required = false) String traineeUsername);
}
