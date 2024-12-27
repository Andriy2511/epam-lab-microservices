package org.example.trainerworkloadservice.DTO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@JsonTypeName("GetTrainerWorkloadRequestDTO")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetTrainerWorkloadRequestDTO {
    private Long trainerId;
    private int trainingYear;
    private int monthNumber;
}
