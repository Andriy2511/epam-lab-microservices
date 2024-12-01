package org.example.finalprojectepamlabapplication.DTO.modelDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.finalprojectepamlabapplication.model.Trainee;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class TraineeDTO {

    private Long id;

    @NotNull(message = "The field cannot be void")
    private Date dateOfBirth;

    @NotBlank(message = "The field cannot be void")
    private String address;

    @NotNull(message = "The field cannot be void")
    private UserDTO userDTO;

    private List<TrainerDTO> trainers;

    private List<TrainingDTO> trainings;

    public static Trainee toEntity(TraineeDTO traineeDTO){
        Trainee trainee = new Trainee();
        trainee.setId(traineeDTO.getId());
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setAddress(traineeDTO.getAddress());
        trainee.setUser(UserDTO.toEntity(traineeDTO.getUserDTO()));
        return trainee;
    }

    public static TraineeDTO toDTO(Trainee trainee){
        return TraineeDTO.builder()
                .id(trainee.getId())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .userDTO(UserDTO.toDTO(trainee.getUser()))
                .build();
    }
}
