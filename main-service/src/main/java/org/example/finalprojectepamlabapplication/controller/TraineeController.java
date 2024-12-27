package org.example.finalprojectepamlabapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerInfoDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface TraineeController {

    @Operation(summary = "Receives trainee by ID",
            description = "The method receives trainee by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    TraineeDTO getTraineeById(@PathVariable Long id);

    @Operation(summary = "Receives trainee",
            description = "The method receives current trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    TraineeDTO getTrainee(@AuthenticationPrincipal GumUserDetails userDetails);

    @Operation(summary = "Updates trainee profile",
            description = "The method updates trainee profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    TraineeDTO updateTrainee(@AuthenticationPrincipal GumUserDetails userDetails, TraineeDTO traineeDTO);

    @Operation(summary = "Deletes trainee",
            description = "The method deletes trainee by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    void deleteTrainee(@AuthenticationPrincipal GumUserDetails userDetails);

    @Operation(summary = "Receives not assigned trainers",
            description = "The methods receives a list of trainers who are not assigned to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    List<TrainerDTO> getTrainersNotAssignedToTrainee(@AuthenticationPrincipal GumUserDetails userDetails);

    @Operation(summary = "Updates user's trainers",
            description = "The method updates the list of trainers based on the trainers' usernames")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainers updated successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    List<TrainerInfoDTO> updateTraineeTrainersList(@AuthenticationPrincipal GumUserDetails userDetails, @ModelAttribute("trainersUsername") List<String> trainerUsernames);

    @Operation(summary = "Finds training with criterion",
            description = "The method finds training assigned to the trainee with a specific criterion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    List<TrainingDTO> getTrainingByTraineeWithCriterion(@AuthenticationPrincipal GumUserDetails userDetails,
                                                                        @RequestParam(name = "to-date", required = false) Date toDate,
                                                                        @RequestParam(name = "from-date", required = false) Date fromDate,
                                                                        @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                                        @RequestParam(name = "trainer-username", required = false) String trainerUsername);
}
