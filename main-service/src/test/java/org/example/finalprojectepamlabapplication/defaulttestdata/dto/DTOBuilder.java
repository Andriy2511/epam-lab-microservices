package org.example.finalprojectepamlabapplication.defaulttestdata.dto;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.*;

import java.util.Date;

public class DTOBuilder {

    public static UserDTO buildUserDTO(Long id){
        return UserDTO.builder()
                .id(id)
                .firstName("Test")
                .lastName("User")
                .username("Test.User")
                .password("0123456789")
                .isActive(true)
                .build();
    }

    public static UserDTO buildUserDTO(Long id, String firstName, String lastName, String username, String password, boolean isActive){
        return UserDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .isActive(isActive)
                .build();
    }

    public static TraineeDTO buildTraineeDTO(Long id, UserDTO userDTO){
        return TraineeDTO.builder()
                .id(id)
                .dateOfBirth(new Date())
                .address("Lviv")
                .userDTO(userDTO)
                .build();
    }

    public static TrainerDTO buildTrainerDTO(Long id, UserDTO userDTO, TrainingTypeDTO trainingTypeDTO){
        return TrainerDTO.builder()
                .id(id)
                .userDTO(userDTO)
                .trainingTypeDTO(trainingTypeDTO)
                .build();
    }

    public static TrainingTypeDTO buildTrainingTypeDTO(Long id, String trainingTypeName){
        return TrainingTypeDTO.builder()
                .id(id)
                .trainingTypeName(trainingTypeName)
                .build();
    }

    public static TrainingDTO buildTrainingDTO(Long id, TraineeDTO traineeDTO, TrainerDTO trainer, TrainingTypeDTO trainingTypeDTO){
        return TrainingDTO.builder()
                .id(id)
                .trainee(traineeDTO)
                .trainer(trainer)
                .trainingName("Test training")
                .trainingTypeDTO(trainingTypeDTO)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
    }
}
