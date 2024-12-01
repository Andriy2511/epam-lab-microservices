package org.example.finalprojectepamlabapplication.controller;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationController {
    @PostMapping("/trainer")
    void registerTrainer(@RequestBody TrainerDTO trainerDTO);

    @PostMapping("/trainee")
    void registerTrainee(@RequestBody TraineeDTO traineeDTO);
}
