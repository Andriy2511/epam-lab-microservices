package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;

import java.util.Date;

@JsonTypeName("TrainerWorkloadRequestDTO")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@Getter
@Builder(toBuilder = true)
public class TrainerWorkloadRequestDTO {
    private Long trainerId;

    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private Date date;

    private int trainingDuration;

    private ActionType actionType;

    public static TrainerWorkloadRequestDTO toTrainerWorkloadRequestDtoFromTrainingDto(TrainingDTO trainingDTO, ActionType actionType){
        return TrainerWorkloadRequestDTO.builder()
                .trainerId(trainingDTO.getTrainer().getId())
                .username(trainingDTO.getTrainer().getUserDTO().getUsername())
                .firstName(trainingDTO.getTrainer().getUserDTO().getFirstName())
                .lastName(trainingDTO.getTrainer().getUserDTO().getLastName())
                .isActive(trainingDTO.getTrainer().getUserDTO().isActive())
                .date(trainingDTO.getTrainingDate())
                .trainingDuration(trainingDTO.getTrainingDuration())
                .actionType(actionType)
                .build();
    }
}
