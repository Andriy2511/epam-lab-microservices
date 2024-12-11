package org.example.trainerworkloadservice.DTO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;

import java.util.Date;

@JsonTypeName("TrainerWorkloadRequestDTO")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainerWorkloadRequestDTO {
    private Long trainerId;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    private boolean isActive;

    @NotNull(message = "Training date cannot be null")
    private Date date;

    @Min(value = 30, message = "Training duration must be at least 1 minute")
    private int trainingDuration;

    @NotNull(message = "Action type cannot be null")
    private ActionType actionType;

    public static TrainerWorkload toTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO){
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerId(1L);
        trainerWorkload.setUsername(trainerWorkloadRequestDTO.getUsername());
        trainerWorkload.setFirstName(trainerWorkloadRequestDTO.getFirstName());
        trainerWorkload.setLastName(trainerWorkloadRequestDTO.getLastName());
        trainerWorkload.setActive(trainerWorkloadRequestDTO.isActive());

        return trainerWorkload;
    }
}
