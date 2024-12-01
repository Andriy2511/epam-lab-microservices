package org.example.finalprojectepamlabapplication.controller.implementation;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.AddTrainingRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.controller.TrainingController;
import org.example.finalprojectepamlabapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainings")
public class TrainingControllerImpl implements TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingControllerImpl(TrainingService trainingService, TraineeService traineeService, TrainerService trainerService, UserService userService, TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    @PostMapping("/{id}")
    public void addTraining(@PathVariable Long id, @ModelAttribute("trainingDTO") AddTrainingRequestDTO addTrainingRequestDTO) {

        TraineeDTO traineeDTO = traineeService.getTraineeByUserId(id);
        TrainerDTO trainerDTO = trainerService.getTrainerById(userService.getUserByUsername(addTrainingRequestDTO.getTrainerUsername()).getId());
        TrainingTypeDTO trainingTypeDTO = trainingTypeService.getTrainingTypeByName(addTrainingRequestDTO.getTrainingTypeName());

        TrainingDTO trainingDTO = TrainingDTO.builder()
                .trainee(traineeDTO)
                .trainer(trainerDTO)
                .trainingName(addTrainingRequestDTO.getTrainingName())
                .trainingTypeDTO(trainingTypeDTO)
                .trainingDate(addTrainingRequestDTO.getTrainingDate())
                .trainingDuration(addTrainingRequestDTO.getTrainingDuration())
                .build();

        trainingService.addTraining(trainingDTO);
    }

    @Override
    @DeleteMapping({"/{id}"})
    public void cancelTraining(@PathVariable Long id) {
        trainingService.deleteTrainingById(id);
    }
}
