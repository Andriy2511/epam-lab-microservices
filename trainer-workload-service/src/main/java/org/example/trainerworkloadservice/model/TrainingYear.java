package org.example.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "training_years")
public class TrainingYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    private int trainingYear;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @EqualsAndHashCode.Exclude
    private TrainerWorkload trainerWorkload;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "trainingYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingMonthSummary> monthSummaries;
}
