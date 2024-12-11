package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainingMonthSummaryResponseDTO {
    private int monthNumber;
    private int totalDuration;
}
