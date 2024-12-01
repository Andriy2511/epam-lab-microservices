package org.example.finalprojectepamlabapplication.service;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.model.TrainingType;

import java.util.Date;
import java.util.List;

public interface TrainingService {

    TrainingDTO addTraining(TrainingDTO trainingDTO);

    TrainingDTO getTrainingById(Long id);

    TrainingDTO getTrainingByName(String name);

    List<TrainingDTO> getTrainingsByTraineeAndCriterion(Long userId, Date toDate, Date fromDate,
                                                        TrainingType trainingType, String trainerUsername);

    List<TrainingDTO> getTrainingsByTrainerAndCriterion(Long userId, Date toDate, Date fromDate,
                                                        TrainingType trainingType, String traineeUsername);

    void deleteTrainingById(Long id);
}
