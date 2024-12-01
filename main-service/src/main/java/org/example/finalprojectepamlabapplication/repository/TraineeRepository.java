package org.example.finalprojectepamlabapplication.repository;

import org.example.finalprojectepamlabapplication.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> deleteTraineeById(Long id);
}
