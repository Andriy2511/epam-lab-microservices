package org.example.finalprojectepamlabapplication.controller.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.controller.TrainerController;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.TrainingService;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/{id}")
    public TrainerDTO getTrainer(@PathVariable Long id) {
        return trainerService.getTrainerByUserId(id);
    }

    @Override
    @PutMapping("/{id}")
    public TrainerDTO updateTrainer(@PathVariable Long id, TrainerDTO trainerDTO) {
        Long trainerId = getTrainerIdByUserId(id);
        trainerDTO.toBuilder().id(trainerId).build();
        return trainerService.updateTrainer(trainerDTO);
    }

    @Override
    @GetMapping("/{id}/trainings")
    public List<TrainingDTO> getTrainingByTrainerWithCriterion(@PathVariable Long id,
                                                                               @RequestParam(name = "to-date", required = false) Date toDate,
                                                                               @RequestParam(name = "from-date", required = false) Date fromDate,
                                                                               @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                                               @RequestParam(name = "trainee-username", required = false) String traineeUsername) {
        TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeByName(trainingTypeName);

        return trainingService
                .getTrainingsByTrainerAndCriterion(id, toDate, fromDate, TrainingTypeDTO.toEntity(trainingTypeDTO), traineeUsername);
    }

    @GetMapping("/{id}/workload")
    public TrainingMonthSummaryResponseDTO getTrainingWorkload(@PathVariable Long id, @RequestParam(name = "year") int year, @RequestParam(name = "month") int month){
        return trainerService.getTrainerWorkload(getTrainerIdByUserId(id), year, month);
    }

    private Long getTrainerIdByUserId(Long userId){
        return trainerService.getTrainerByUserId(userId).getId();
    }
}
