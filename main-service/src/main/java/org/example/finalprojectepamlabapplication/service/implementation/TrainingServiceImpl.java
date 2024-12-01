package org.example.finalprojectepamlabapplication.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ActionType;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.client.TrainerWorkloadClient;
import org.example.finalprojectepamlabapplication.model.TrainingType;
import org.example.finalprojectepamlabapplication.repository.TrainingRepository;
import org.example.finalprojectepamlabapplication.model.Training;
import org.example.finalprojectepamlabapplication.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainerWorkloadClient trainerWorkloadClient;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TrainerWorkloadClient trainerWorkloadClient) {
        this.trainingRepository = trainingRepository;
        this.trainerWorkloadClient = trainerWorkloadClient;
    }

    @Override
    public TrainingDTO addTraining(TrainingDTO trainingDTO) {
        Training training = TrainingDTO.toEntity(trainingDTO);
        trainingDTO = TrainingDTO.toDTO(trainingRepository.save(training));

        trainerWorkloadClient.updateTrainerWorkload(trainingDTO, ActionType.ADD);

        return trainingDTO;
    }

    @Override
    public TrainingDTO getTrainingById(Long id) {
        log.info("Searching training with id: {}", id);
        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty()){
            log.warn("Training with id {} not found.", id);
        }
        return TrainingDTO.toDTO(training.orElseThrow());
    }

    @Override
    public TrainingDTO getTrainingByName(String name) {
        Optional<Training> training = trainingRepository.findTrainingByTrainingName(name);
        if (training.isEmpty()){
            log.warn("Training with name {} not found.", training);
        }
        return TrainingDTO.toDTO(training.orElseThrow());
    }

    @Override
    public List<TrainingDTO> getTrainingsByTraineeAndCriterion(Long userId, Date toDate, Date fromDate,
                                                               TrainingType trainingType, String trainerUsername){
        List<Training> trainings = trainingRepository.findTrainingsByTraineeAndCriterion(userId, toDate, fromDate, trainingType, trainerUsername);
        return trainings.stream().map(TrainingDTO::toDTO).toList();
    }

    @Override
    public List<TrainingDTO> getTrainingsByTrainerAndCriterion(Long userId, Date toDate, Date fromDate,
                                                               TrainingType trainingType, String traineeUsername){
        List<Training> trainings = trainingRepository.findTrainingsByTrainerAndCriterion(userId, toDate, fromDate, trainingType, traineeUsername);
        return trainings.stream().map(TrainingDTO::toDTO).toList();
    }

    @Override
    public void deleteTrainingById(Long id) {
        TrainingDTO trainingDTO = getTrainingById(id);
        trainingRepository.deleteById(id);
        trainerWorkloadClient.updateTrainerWorkload(trainingDTO, ActionType.DELETE);
    }
}
