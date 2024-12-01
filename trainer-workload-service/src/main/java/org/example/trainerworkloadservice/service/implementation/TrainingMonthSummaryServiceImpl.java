package org.example.trainerworkloadservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainingMonthSummaryRepository;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TrainingMonthSummaryServiceImpl implements TrainingMonthSummaryService {

    private final TrainingMonthSummaryRepository trainingMonthSummaryRepository;

    @Autowired
    public TrainingMonthSummaryServiceImpl(TrainingMonthSummaryRepository trainingMonthSummaryRepository) {
        this.trainingMonthSummaryRepository = trainingMonthSummaryRepository;
    }

    @Override
    public Optional<TrainingMonthSummary> getMonthByMonthNumberAndTrainingYear(int monthNumber, TrainingYear trainingYear) {
        Optional<TrainingMonthSummary> trainingMonthSummary = trainingMonthSummaryRepository.findByMonthNumberAndTrainingYear(monthNumber, trainingYear);
        if(trainingMonthSummary.isEmpty()){
            log.debug("No month summary found for training year with id {} and month number {}", trainingYear.getId(), monthNumber);
        }
        return trainingMonthSummary;
    }

    @Override
    public TrainingMonthSummary updateTrainingMonthSummary(TrainingMonthSummary trainingMonthSummary){
        return trainingMonthSummaryRepository.save(trainingMonthSummary);
    }

    @Override
    public TrainingMonthSummary getByTrainerIdAndYearAndMonthNumber(Long trainerId, int year, int monthNumber) {
        Optional<TrainingMonthSummary> trainingMonthSummary =
                trainingMonthSummaryRepository.findByTrainerIdAndTrainingYearAndMonthNumber(trainerId, year, monthNumber);
        if(trainingMonthSummary.isEmpty()){
            log.debug("No month summary found with trainerId {}, year {}, and month number {}", trainerId, year, monthNumber);
        }
        return trainingMonthSummary.orElseThrow();
    }
}
