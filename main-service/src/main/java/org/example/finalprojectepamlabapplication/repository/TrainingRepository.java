package org.example.finalprojectepamlabapplication.repository;

import org.example.finalprojectepamlabapplication.model.Training;
import org.example.finalprojectepamlabapplication.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findTrainingByTrainingName(String trainingName);

    @Query("""
            SELECT t FROM Training t WHERE t.trainee.user.id = :id AND
            (:toDate IS NULL OR t.trainingDate <= :toDate) AND
            (:fromDate IS NULL OR t.trainingDate >= :fromDate) AND
            (:trainingType IS NULL OR t.trainingType = :trainingType) AND
            (:traineeUsername IS NULL OR t.trainer.user.username = :trainerUsername)""")
    List<Training> findTrainingsByTraineeAndCriterion(
            @Param("id") Long id,
            @Param("toDate") Date toDate,
            @Param("fromDate") Date fromDate,
            @Param("trainingType") TrainingType trainingType,
            @Param("trainerUsername") String trainerUsername
    );

    @Query("""
            SELECT t FROM Training t WHERE t.trainer.user.id = :id AND
            (:toDate IS NULL OR t.trainingDate <= :toDate) AND
            (:fromDate IS NULL OR t.trainingDate >= :fromDate) AND
            (:trainingType IS NULL OR t.trainingType = :trainingType) AND
            (:traineeUsername IS NULL OR t.trainee.user.username = :traineeUsername)""")
    List<Training> findTrainingsByTrainerAndCriterion(
            @Param("id") Long id,
            @Param("toDate") Date toDate,
            @Param("fromDate") Date fromDate,
            @Param("trainingType") TrainingType trainingType,
            @Param("traineeUsername") String traineeUsername
    );
}
