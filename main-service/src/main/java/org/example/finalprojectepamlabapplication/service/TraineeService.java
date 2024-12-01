package org.example.finalprojectepamlabapplication.service;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;

import java.util.List;

public interface TraineeService {

    TraineeDTO addTrainee(TraineeDTO traineeDTO);

    TraineeDTO updateTrainee(TraineeDTO traineeDTO);

    TraineeDTO deleteTrainee(Long id);

    TraineeDTO getTraineeById(Long id);

    TraineeDTO updateTrainersListByTraineeId(List<TrainerDTO> trainerDTOList, Long traineeId);

    TraineeDTO getTraineeByUserId(Long userId);
}
