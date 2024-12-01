package org.example.trainerworkloadservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainingYearRepository;
import org.example.trainerworkloadservice.service.TrainingYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainingYearServiceImpl implements TrainingYearService {

    private final TrainingYearRepository trainingYearRepository;

    @Autowired
    public TrainingYearServiceImpl(TrainingYearRepository trainingYearRepository) {
        this.trainingYearRepository = trainingYearRepository;
    }

    @Override
    public Optional<TrainingYear> getByTrainingYearAndTrainerWorkload(int trainingYear, TrainerWorkload trainerWorkload){
        Optional<TrainingYear> year = trainingYearRepository.findByTrainingYearAndTrainerWorkload(trainingYear, trainerWorkload);
        if(year.isEmpty()){
            log.debug("No training year found for trainer with id {} and training year {}", trainerWorkload.getTrainerId(), trainingYear);
        }
        return year;
    }

    @Override
    public TrainingYear addTrainingYear(TrainingYear trainingYear) {
        return trainingYearRepository.save(trainingYear);
    }
}
