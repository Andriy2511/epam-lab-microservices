package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrainingRequestDTO {

    @NotBlank(message = "Trainer id is required")
    private String trainerUsername;

    @NotBlank(message = "Training Name is required")
    @Size(min = 1, message = "Training Name cannot be empty")
    private String trainingName;

    @NotNull(message = "Training Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;

    @NotBlank(message = "Training Type is required")
    @Size(min = 1, message = "Training Type cannot be empty")
    private String trainingTypeName;

    @NotNull(message = "Training Duration is required")
    @Positive(message = "Training Duration must be positive")
    private Integer trainingDuration;
}
