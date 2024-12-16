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
import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.utility.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonTypeName("TrainerWorkloadRequestDTO")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
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
        trainerWorkload.setTrainerId(trainerWorkloadRequestDTO.getTrainerId());
        trainerWorkload.setUsername(trainerWorkloadRequestDTO.getUsername());
        trainerWorkload.setFirstName(trainerWorkloadRequestDTO.getFirstName());
        trainerWorkload.setLastName(trainerWorkloadRequestDTO.getLastName());
        trainerWorkload.setActive(trainerWorkloadRequestDTO.isActive());
        trainerWorkload.setTrainingYears(setTrainingYears(createNewTrainingYear(trainerWorkloadRequestDTO)));

        return trainerWorkload;
    }

    private static TrainingYear createNewTrainingYear(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        TrainingYear trainingYear = new TrainingYear();
        trainingYear.setTrainingYear(DateConverter.getYearAsInteger(trainerWorkloadRequestDTO.getDate()));
        trainingYear.setMonths(setMonthSummaryList(createNewTrainingMonthSummary(trainerWorkloadRequestDTO)));
        return trainingYear;
    }

    private static TrainingMonthSummary createNewTrainingMonthSummary(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        TrainingMonthSummary trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(DateConverter.getMonthAsInteger(trainerWorkloadRequestDTO.getDate()));
        trainingMonthSummary.setTotalDuration(trainerWorkloadRequestDTO.getTrainingDuration());
        return trainingMonthSummary;
    }

    private static List<TrainingYear> setTrainingYears(TrainingYear trainingYear) {
        List<TrainingYear> trainingYears = new ArrayList<>();
        trainingYears.add(trainingYear);
        return trainingYears;
    }

    private static List<TrainingMonthSummary> setMonthSummaryList(TrainingMonthSummary monthSummary) {
        List<TrainingMonthSummary> monthSummaryList = new ArrayList<>();
        monthSummaryList.add(monthSummary);
        return monthSummaryList;
    }
}
