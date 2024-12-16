package org.example.trainerworkloadservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "trainer_workloads")
public class TrainerWorkload {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long trainerId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;

    @EqualsAndHashCode.Exclude
    private List<TrainingYear> trainingYears;
}
