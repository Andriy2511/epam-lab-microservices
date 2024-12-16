package org.example.trainerworkloadservice.repository;

import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, String> {
    Optional<TrainerWorkload> findByTrainerId(Long trainerId);
}
