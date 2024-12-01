package org.example.trainerworkloadservice.service;

import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.model.TrainerWorkload;

public interface TrainerWorkloadService {
    void updateTimeForTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO);

    TrainerWorkload addTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO);
}
