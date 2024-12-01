package org.example.finalprojectepamlabapplication.controller.implementation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
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
    private final UserService userService;

    @Autowired
    public RegistrationControllerImpl(TraineeService traineeService, TrainerService trainerService, UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @Override
    @PostMapping("/trainer")
    public void registerTrainer(@Valid @RequestBody TrainerDTO trainerDTO){
        String username = trainerDTO.getUserDTO().getUsername();
        if(!isUserExists(username)) {
            trainerService.addTrainer(trainerDTO);
        }  else {
            log.debug("The user {} already exists", username);
            throw new UserAlreadyExistsException("The user already exists");
        }
    }

    @Override
    @PostMapping("/trainee")
    public void registerTrainee(@Valid @RequestBody TraineeDTO traineeDTO){
        String username = traineeDTO.getUserDTO().getUsername();
        if(!isUserExists(username)) {
            traineeService.addTrainee(traineeDTO);
        }  else {
            log.debug("The user {} already exists", username);
            throw new UserAlreadyExistsException("The user already exists");
        }
    }

    private boolean isUserExists(String username){
        return userService.getUserByUsername(username) != null;
    }
}
