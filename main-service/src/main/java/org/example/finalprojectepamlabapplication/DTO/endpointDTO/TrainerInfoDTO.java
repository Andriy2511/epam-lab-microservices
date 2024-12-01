package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerInfoDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeDTO specialization;

    public static TrainerInfoDTO toTrainerInfoDTO(TrainerDTO trainerDTO) {
        TrainerInfoDTO trainerInfoDTO = new TrainerInfoDTO();
        trainerInfoDTO.setId(trainerDTO.getId());
        trainerInfoDTO.setUsername(trainerDTO.getUserDTO().getUsername());
        trainerInfoDTO.setFirstName(trainerDTO.getUserDTO().getFirstName());
        trainerInfoDTO.setLastName(trainerDTO.getUserDTO().getLastName());
        trainerInfoDTO.setSpecialization(trainerDTO.getTrainingTypeDTO());
        return trainerInfoDTO;
    }
}
