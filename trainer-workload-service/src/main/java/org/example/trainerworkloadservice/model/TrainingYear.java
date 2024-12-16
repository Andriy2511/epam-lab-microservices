package org.example.trainerworkloadservice.model;

import lombok.*;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TrainingYear {

    @Field("trainingYear")
    private int trainingYear;

    @Field("months")
    private List<TrainingMonthSummary> months;
}
