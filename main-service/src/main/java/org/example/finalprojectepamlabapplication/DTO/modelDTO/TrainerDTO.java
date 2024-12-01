package org.example.finalprojectepamlabapplication.DTO.modelDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.finalprojectepamlabapplication.model.Trainer;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class TrainerDTO {

    private Long id;

    @Valid
    @NotNull(message = "The field cannot be void")
    private TrainingTypeDTO trainingTypeDTO;

    @Valid
    @NotNull(message = "The field cannot be void")
    private UserDTO userDTO;

    private List<TraineeDTO> trainees;

    private List<TrainingDTO> trainings;

    public static Trainer toEntity(TrainerDTO trainerDTO) {
        Trainer trainer = new Trainer();
        trainer.setId(trainerDTO.getId());
        trainer.setTrainingType(TrainingTypeDTO.toEntity(trainerDTO.getTrainingTypeDTO()));
        trainer.setUser(UserDTO.toEntity(trainerDTO.getUserDTO()));
        return trainer;
    }

    public static TrainerDTO toDTO(Trainer trainer) {
        return TrainerDTO.builder()
                .id(trainer.getId())
                .trainingTypeDTO(TrainingTypeDTO.toDTO(trainer.getTrainingType()))
                .userDTO(UserDTO.toDTO(trainer.getUser()))
                .build();
    }
}
