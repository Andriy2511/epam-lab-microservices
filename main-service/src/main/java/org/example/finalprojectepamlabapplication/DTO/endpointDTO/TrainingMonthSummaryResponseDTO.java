package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingMonthSummaryResponseDTO {
    private int monthNumber;
    private int totalDuration;
}
