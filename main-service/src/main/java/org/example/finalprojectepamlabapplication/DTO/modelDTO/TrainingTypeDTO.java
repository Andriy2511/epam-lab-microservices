package org.example.finalprojectepamlabapplication.DTO.modelDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.finalprojectepamlabapplication.model.TrainingType;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class TrainingTypeDTO {

    private Long id;

    @NotBlank(message = "The field cannot be void")
    private String trainingTypeName;

    private List<TrainingDTO> trainings;

    private List<TrainerDTO> trainers;

    public static TrainingType toEntity(TrainingTypeDTO trainingTypeDTO) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeDTO.getId());
        trainingType.setTrainingTypeName(trainingTypeDTO.getTrainingTypeName());
        return trainingType;
    }

    public static TrainingTypeDTO toDTO(TrainingType trainingType) {
        return TrainingTypeDTO.builder()
                .id(trainingType.getId())
                .trainingTypeName(trainingType.getTrainingTypeName())
                .build();
    }
}
