package org.example.trainerworkloadservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainingMonthSummary {
    @Field("monthNumber")
    private int monthNumber;

    @Field("totalDuration")
    private int totalDuration;
}
