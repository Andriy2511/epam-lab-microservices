package org.example.trainerworkloadservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.repository.TrainingMonthSummaryCustomRepository;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainingMonthSummaryServiceImpl implements TrainingMonthSummaryService {

    private final TrainingMonthSummaryCustomRepository repository;

    @Autowired
    public TrainingMonthSummaryServiceImpl(TrainingMonthSummaryCustomRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrainingMonthSummary getByTrainerIdAndYearAndMonthNumber(Long trainerId, int year, int monthNumber) {
        Optional<TrainingMonthSummary> trainingMonthSummary =
                repository.findTrainingMonthSummary(trainerId, year, monthNumber);
        if(trainingMonthSummary.isEmpty()){
            log.debug("No month summary found with trainerId {}, year {}, and month number {}", trainerId, year, monthNumber);
        }

        return trainingMonthSummary.orElseThrow();
    }
}
