package org.example.finalprojectepamlabapplication.controller.implementation;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerInfoDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.controller.TraineeController;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping
    public TraineeDTO getTrainee(@AuthenticationPrincipal GumUserDetails userDetails){
        return traineeService.getTraineeByUserUsername(userDetails.getUsername());
    }

    @Override
    @PutMapping
    public TraineeDTO updateTrainee(@AuthenticationPrincipal GumUserDetails userDetails, TraineeDTO traineeDTO) {
        Long traineeId = getTraineeIdByUser(userDetails);
        traineeDTO.toBuilder().id(traineeId).build();
        return traineeService.updateTrainee(traineeDTO);
    }

    @Override
    @DeleteMapping
    public void deleteTrainee(@AuthenticationPrincipal GumUserDetails userDetails) {
        Long traineeId = getTraineeIdByUser(userDetails);
        traineeService.deleteTrainee(traineeId);
    }

    @Override
    @GetMapping("/not-assigned-trainers")
    public List<TrainerDTO> getTrainersNotAssignedToTrainee(@AuthenticationPrincipal GumUserDetails userDetails) {
        Long traineeId = getTraineeIdByUser(userDetails);
        return trainerService.getTrainersNotAssignedToTrainee(traineeId);
    }

    @Override
    @PutMapping("/trainers")
    public List<TrainerInfoDTO> updateTraineeTrainersList(@AuthenticationPrincipal GumUserDetails userDetails, @ModelAttribute("trainersUsername") List<String> trainerUsernames) {
        List<TrainerDTO> updatedTrainersList = new ArrayList<>();
        for (String trainerUsername : trainerUsernames) {
            updatedTrainersList.add(userService.getUserByUsername(trainerUsername).getTrainerDTO());
        }

        Long traineeId = getTraineeIdByUser(userDetails);
        traineeService.updateTrainersListByTraineeId(updatedTrainersList, traineeId);

        return updatedTrainersList.stream().map(TrainerInfoDTO::toTrainerInfoDTO).toList();
    }

    @Override
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainingByTraineeWithCriterion(@AuthenticationPrincipal GumUserDetails userDetails,
                                                               @RequestParam(name = "to-date", required = false) Date toDate,
                                                               @RequestParam(name = "from-date", required = false) Date fromDate,
                                                               @RequestParam(name = "training-type-name", required = false) String trainingTypeName,
                                                               @RequestParam(name = "trainer-username", required = false) String trainerUsername) {
        TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeByName(trainingTypeName);

        return trainingService
                .getTrainingsByTraineeAndCriterion(userService.getUserByUsername(userDetails.getUsername()).getId(), toDate, fromDate, TrainingTypeDTO.toEntity(trainingTypeDTO), trainerUsername);
    }

    private Long getTraineeIdByUser(GumUserDetails userDetails){
        return traineeService.getTraineeByUserUsername(userDetails.getUsername()).getId();
    }
}
