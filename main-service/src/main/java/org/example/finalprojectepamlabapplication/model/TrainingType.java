package org.example.finalprojectepamlabapplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainingTypeName;

    @OneToMany(mappedBy = "trainingType")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Training> trainings;

    @OneToMany(mappedBy = "trainingType")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Trainer> trainers;
}
