package org.example.trainerworkloadservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainerWorkloadRepository;
import org.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.example.trainerworkloadservice.service.TrainingYearService;
import org.example.trainerworkloadservice.utility.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadRepository trainerWorkloadRepository;
    private final TrainingMonthSummaryService trainingMonthSummaryService;
    private final TrainingYearService trainingYearService;

    @Autowired
    public TrainerWorkloadServiceImpl(TrainerWorkloadRepository trainerWorkloadRepository, TrainingMonthSummaryService trainingMonthSummaryService, TrainingYearService trainingYearService) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
        this.trainingMonthSummaryService = trainingMonthSummaryService;
        this.trainingYearService = trainingYearService;
    }

    @Override
    @Transactional
    public void updateTimeForTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        TrainerWorkload trainerWorkload = getOrCreateTrainerWorkload(trainerWorkloadRequestDTO);
        TrainingYear trainingYear = getOrCreateTrainingYear(trainerWorkload, trainerWorkloadRequestDTO.getDate());
        TrainingMonthSummary trainingMonthSummary = getOrCreateTrainingMonthSummary(trainingYear, trainerWorkloadRequestDTO.getDate());

        updateTrainingTime(trainingMonthSummary, trainerWorkloadRequestDTO.getActionType(), trainerWorkloadRequestDTO.getTrainingDuration());
    }

    @Override
    public TrainerWorkload addTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO){
        return trainerWorkloadRepository.save(TrainerWorkloadRequestDTO.toTrainerWorkload(trainerWorkloadRequestDTO));
    }

    private TrainerWorkload getOrCreateTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        return trainerWorkloadRepository.findByTrainerId(trainerWorkloadRequestDTO.getTrainerId())
                .orElseGet(() -> addTrainerWorkload(trainerWorkloadRequestDTO));
    }

    private TrainingYear getOrCreateTrainingYear(TrainerWorkload trainerWorkload, Date date) {
        return findTrainingYearByTrainerWorkloadAndDate(trainerWorkload, date)
                .orElseGet(() -> trainingYearService.addTrainingYear(createNewTrainingYear(trainerWorkload, date)));
    }

    private TrainingMonthSummary getOrCreateTrainingMonthSummary(TrainingYear trainingYear, Date date) {
        return findTrainingMonthSummaryByTrainingYearAndDate(trainingYear, date)
                .orElseGet(() -> trainingMonthSummaryService.updateTrainingMonthSummary(createNewTrainingMonthSummary(trainingYear, date)));
    }

    private void updateTrainingTime(TrainingMonthSummary summary, ActionType actionType, int duration) {
        if (actionType.equals(ActionType.ADD)) {
            addTimeToTraining(summary, duration);
        } else {
            subtractTimeFromTraining(summary, duration);
        }
    }

    private void addTimeToTraining(TrainingMonthSummary trainingMonthSummary, int duration){
        trainingMonthSummary.setTotalDuration(trainingMonthSummary.getTotalDuration() + duration);
        trainingMonthSummaryService.updateTrainingMonthSummary(trainingMonthSummary);
    }

    private void subtractTimeFromTraining(TrainingMonthSummary trainingMonthSummary, int duration){
        trainingMonthSummary.setTotalDuration(trainingMonthSummary.getTotalDuration() - duration);
        trainingMonthSummaryService.updateTrainingMonthSummary(trainingMonthSummary);
    }

    private Optional<TrainingYear> findTrainingYearByTrainerWorkloadAndDate(TrainerWorkload trainerWorkload, Date date){
        return trainingYearService.getByTrainingYearAndTrainerWorkload(
                DateConverter.getYearAsInteger(date),
                trainerWorkload);
    }

    private Optional<TrainingMonthSummary> findTrainingMonthSummaryByTrainingYearAndDate(TrainingYear trainingYear, Date date){
        return trainingMonthSummaryService.getMonthByMonthNumberAndTrainingYear(
                DateConverter.getMonthAsInteger(date),
                trainingYear);
    }

    private TrainingYear createNewTrainingYear(TrainerWorkload trainerWorkload, Date date) {
        TrainingYear trainingYear = new TrainingYear();
        trainingYear.setTrainerWorkload(trainerWorkload);
        trainingYear.setTrainingYear(DateConverter.getYearAsInteger(date));
        return trainingYear;
    }

    private TrainingMonthSummary createNewTrainingMonthSummary(TrainingYear trainingYear, Date date) {
        TrainingMonthSummary trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(DateConverter.getMonthAsInteger(date));
        trainingMonthSummary.setTrainingYear(trainingYear);
        return trainingMonthSummary;
    }
}
