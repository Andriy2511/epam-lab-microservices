package org.example.trainerworkloadservice.DTO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("GetTrainerWorkloadRequestDTO")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerWorkloadRequestDTO {
    private Long trainerId;
    private int trainingYear;
    private int monthNumber;
}
