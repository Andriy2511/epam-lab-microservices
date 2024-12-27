package org.example.finalprojectepamlabapplication.controller.implementation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.controller.RegistrationController;
import org.example.finalprojectepamlabapplication.exception.UserAlreadyExistsException;
import org.example.finalprojectepamlabapplication.service.TraineeService;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/registration")
public class RegistrationControllerImpl implements RegistrationController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public RegistrationControllerImpl(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Override
    @PostMapping("/trainer")
    public TrainerDTO registerTrainer(@Valid @RequestBody TrainerDTO trainerDTO){
        return trainerService.addTrainer(trainerDTO);
    }

    @Override
    @PostMapping("/trainee")
    public TraineeDTO registerTrainee(@Valid @RequestBody TraineeDTO traineeDTO){
        return traineeService.addTrainee(traineeDTO);
    }
}
