package org.example.trainerworkloadservice.repository;

import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingYearRepository extends JpaRepository<TrainingYear, Long> {
    Optional<TrainingYear> findByTrainingYearAndTrainerWorkload(int trainingYear, TrainerWorkload trainerWorkload);
}
