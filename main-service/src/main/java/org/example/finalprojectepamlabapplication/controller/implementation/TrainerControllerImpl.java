package org.example.finalprojectepamlabapplication.controller.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.controller.TrainerController;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.TrainingService;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainers")
@Slf4j
public class TrainerControllerImpl implements TrainerController {

    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;

    @Autowired
    public TrainerControllerImpl(TrainerService trainerService, TrainingTypeService trainingTypeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.trainingService = trainingService;
    }

    @Override
    @GetMapping
    public TrainerDTO getTrainer(@AuthenticationPrincipal GumUserDetails userDetails) {
        return trainerService.getTrainerByUsername(userDetails.getUsername());
    }

    @Override
    @PutMapping
    public TrainerDTO updateTrainer(@AuthenticationPrincipal GumUserDetails userDetails, TrainerDTO trainerDTO) {
        Long trainerId = getTrainerIdByUser(userDetails);
        trainerDTO.toBuilder().id(trainerId).build();
        return trainerService.updateTrainer(trainerDTO);
    }

    @Override
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainingByTrainerWithCriterion(@AuthenticationPrincipal GumUserDetails userDetails,
                                                                               @RequestParam(name = "to-date", required = false) Date toDate,
                                                                               @RequestParam(name = "from-date", required = false) Date fromDate,
                                                                               @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                                               @RequestParam(name = "trainee-username", required = false) String traineeUsername) {
        TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeByName(trainingTypeName);

        return trainingService
                .getTrainingsByTrainerAndCriterion(getTrainer(userDetails).getUserDTO().getId(), toDate, fromDate, TrainingTypeDTO.toEntity(trainingTypeDTO), traineeUsername);
    }

    @Override
    @GetMapping("/workload")
    public TrainingMonthSummaryResponseDTO getTrainingWorkload(@AuthenticationPrincipal GumUserDetails userDetails, @RequestParam(name = "year") int year, @RequestParam(name = "month") int month){
        String username = userDetails.getUsername();
        return trainerService.getTrainerWorkload(trainerService.getTrainerByUsername(username).getId(), year, month);
    }

    private Long getTrainerIdByUser(GumUserDetails userDetails){
        return trainerService.getTrainerByUsername(userDetails.getUsername()).getId();
    }
}
