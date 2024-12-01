package org.example.finalprojectepamlabapplication.controller.implementation;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerInfoDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.controller.TraineeController;
import org.example.finalprojectepamlabapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainees")
public class TraineeControllerImpl implements TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    @Autowired
    public TraineeControllerImpl(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, TrainingTypeService trainingTypeService, UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}")
    public TraineeDTO getTraineeById(@PathVariable Long id) {
        return traineeService.getTraineeByUserId(id);
    }

    @Override
    @PutMapping("/{id}")
    public TraineeDTO updateTrainee(@PathVariable Long id, TraineeDTO traineeDTO) {
        Long traineeId = getTraineeIdByUserId(id);
        traineeDTO.toBuilder().id(traineeId).build();
        return traineeService.updateTrainee(traineeDTO);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteTrainee(@PathVariable Long id) {
        Long traineeId = getTraineeIdByUserId(id);
        traineeService.deleteTrainee(traineeId);
    }

    @Override
    @GetMapping("/{id}/not-assigned-trainers")
    public List<TrainerDTO> getTrainersNotAssignedToTrainee(@PathVariable Long id) {
        Long traineeId = getTraineeIdByUserId(id);
        return trainerService.getTrainersNotAssignedToTrainee(traineeId);
    }

    @Override
    @PutMapping("/{id}/trainers")
    public List<TrainerInfoDTO> updateTraineeTrainersList(@PathVariable Long id, @ModelAttribute("trainersUsername") List<String> trainerUsernames) {
        List<TrainerDTO> updatedTrainersList = new ArrayList<>();
        for (String trainerUsername : trainerUsernames) {
            updatedTrainersList.add(userService.getUserByUsername(trainerUsername).getTrainerDTO());
        }

        Long traineeId = getTraineeIdByUserId(id);
        traineeService.updateTrainersListByTraineeId(updatedTrainersList, traineeId);

        return updatedTrainersList.stream().map(TrainerInfoDTO::toTrainerInfoDTO).toList();
    }

    @Override
    @GetMapping("/{id}/trainings")
    public List<TrainingDTO> getTrainingByTraineeWithCriterion(@PathVariable Long id,
                                                                               @RequestParam(name = "to-date", required = false) Date toDate,
                                                                               @RequestParam(name = "from-date", required = false) Date fromDate,
                                                                               @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                                               @RequestParam(name = "trainer-username", required = false) String trainerUsername) {
        TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeByName(trainingTypeName);

        return trainingService
                .getTrainingsByTraineeAndCriterion(id, toDate, fromDate, TrainingTypeDTO.toEntity(trainingTypeDTO), trainerUsername);
    }

    private Long getTraineeIdByUserId(Long userId){
        return traineeService.getTraineeByUserId(userId).getId();
    }
}
