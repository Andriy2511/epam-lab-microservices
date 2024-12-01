package org.example.finalprojectepamlabapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.AddTrainingRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface TrainingController {

    @Operation(summary = "Adds training for the user",
            description = "The method adds training for the user by user ID and training details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training added successfully"),
            @ApiResponse(responseCode = "400", description = "Trainee, trainer or training type not found")
    })
    void addTraining(@PathVariable Long id, @ModelAttribute("trainingDTO") AddTrainingRequestDTO addTrainingRequestDTO);

    @DeleteMapping
    void cancelTraining(@PathVariable Long id);
}
