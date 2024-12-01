package org.example.trainerworkloadservice.repository;

import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByTrainerId(Long id);
}
