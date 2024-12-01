package org.example.finalprojectepamlabapplication.repository;

import org.example.finalprojectepamlabapplication.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> deleteTrainerById(Long id);

    @Query("SELECT tr FROM Trainer tr LEFT JOIN tr.trainees tn WHERE tn IS NULL OR tn.user.id != :id")
    List<Trainer> findTrainersWhichNotAssignToTraineeByUserId(@Param("id") Long id);

}
