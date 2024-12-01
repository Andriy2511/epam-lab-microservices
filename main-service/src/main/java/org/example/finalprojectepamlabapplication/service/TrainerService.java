package org.example.finalprojectepamlabapplication.service;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;

import java.util.List;

public interface TrainerService {

    TrainerDTO addTrainer(TrainerDTO trainerDTO);

    TrainerDTO updateTrainer(TrainerDTO trainerDTO);

    TrainerDTO deleteTrainer(Long id);

    TrainerDTO getTrainerById(Long id);

    List<TrainerDTO> getTrainersNotAssignedToTrainee(Long id);

    TrainerDTO getTrainerByUserId(Long userId);

    TrainingMonthSummaryResponseDTO getTrainerWorkload(Long id, int year, int month);
}
