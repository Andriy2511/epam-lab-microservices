package org.example.finalprojectepamlabapplication.DTO.modelDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.finalprojectepamlabapplication.model.Training;

import java.util.Date;

@Getter
@ToString
@Builder(toBuilder = true)
public class TrainingDTO {

    private Long id;

    @NotNull(message = "The field cannot be void")
    private TraineeDTO trainee;

    @NotNull(message = "The field cannot be void")
    private TrainerDTO trainer;

    @NotBlank(message = "The field cannot be void")
    private String trainingName;

    @NotNull(message = "The field cannot be void")
    private TrainingTypeDTO trainingTypeDTO;

    @NotNull(message = "The field cannot be void")
    private Date trainingDate;

    @Min(value = 30, message = "The minimum training duration is 30 minutes")
    private Integer trainingDuration;

    public static Training toEntity(TrainingDTO trainingDTO){
        Training training = new Training();
        training.setId(trainingDTO.getId());
        training.setTrainee(TraineeDTO.toEntity(trainingDTO.getTrainee()));
        training.setTrainer(TrainerDTO.toEntity(trainingDTO.getTrainer()));
        training.setTrainingName(trainingDTO.getTrainingName());
        training.setTrainingType(TrainingTypeDTO.toEntity(trainingDTO.getTrainingTypeDTO()));
        training.setTrainingDate(trainingDTO.getTrainingDate());
        training.setTrainingDuration(trainingDTO.getTrainingDuration());
        return training;
    }

    public static TrainingDTO toDTO(Training training){
        return TrainingDTO.builder()
                .id(training.getId())
                .trainee(TraineeDTO.toDTO(training.getTrainee()))
                .trainer(TrainerDTO.toDTO(training.getTrainer()))
                .trainingName(training.getTrainingName())
                .trainingTypeDTO(TrainingTypeDTO.toDTO(training.getTrainingType()))
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .build();
    }
}
