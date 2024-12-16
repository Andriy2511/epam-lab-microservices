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
import org.example.trainerworkloadservice.utility.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadRepository trainerWorkloadRepository;
    private final TrainingMonthSummaryService trainingMonthSummaryService;

    @Autowired
    public TrainerWorkloadServiceImpl(TrainerWorkloadRepository trainerWorkloadRepository, TrainingMonthSummaryService trainingMonthSummaryService) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
        this.trainingMonthSummaryService = trainingMonthSummaryService;
    }

    @Override
    public void updateTimeForTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        Optional<TrainerWorkload> workload = trainerWorkloadRepository.findByTrainerId(trainerWorkloadRequestDTO.getTrainerId());
        if (workload.isPresent()) {
            updateTrainingTime(workload.get(), trainerWorkloadRequestDTO);
        } else {
            log.info("createNewTrainerWorkload");
            createNewTrainerWorkload(trainerWorkloadRequestDTO);
        }
    }

    private void updateTrainingTime(TrainerWorkload workload, TrainerWorkloadRequestDTO requestDTO) {
        int year = DateConverter.getYearAsInteger(requestDTO.getDate());
        int monthNumber = DateConverter.getMonthAsInteger(requestDTO.getDate());
        if(isMonthNumberInYearExist(workload, year, monthNumber)){
            if (requestDTO.getActionType().equals(ActionType.ADD)) {
                addTimeToTraining(workload, year, monthNumber, requestDTO.getTrainingDuration());
            } else {
                subtractTimeFromTraining(workload, year, monthNumber, requestDTO.getTrainingDuration());
            }
        } else {
            setNewTrainingDurationInMonthAndYearIfDontExist(workload, year, monthNumber, requestDTO);
        }
    }

    private void setNewTrainingDurationInMonthAndYearIfDontExist(
            TrainerWorkload workload, int year, int monthNumber, TrainerWorkloadRequestDTO requestDTO){
        if(isYearExist(workload, year)){
            setMonthForWorkloadIfYearExist(workload, year, monthNumber, requestDTO.getTrainingDuration());
        } else {
            setMonthAndYearIfDontExist(workload, requestDTO);
        }

        trainerWorkloadRepository.save(workload);
    }

    private void setMonthAndYearIfDontExist(TrainerWorkload workload, TrainerWorkloadRequestDTO requestDTO){
        TrainerWorkload convertedWorkload = TrainerWorkloadRequestDTO.toTrainerWorkload(requestDTO);
        workload.getTrainingYears().add(convertedWorkload.getTrainingYears().getFirst());
    }

    private void setMonthForWorkloadIfYearExist(TrainerWorkload workload, int year, int monthNumber, int trainingDuration) {
        TrainingYear trainingYear = findYearByNumber(workload.getTrainingYears(), year).get();
        workload.getTrainingYears().remove(trainingYear);

        List<TrainingMonthSummary> monthSummaryList = trainingYear.getMonths();
        monthSummaryList.add(new TrainingMonthSummary(monthNumber, trainingDuration));

        trainingYear.setMonths(monthSummaryList);
        workload.getTrainingYears().add(trainingYear);
    }

    private void addTimeToTraining(TrainerWorkload workload, int year, int month, int duration){
        updateTrainingDuration(workload, year, month, duration, (monthSummary, time) ->
                monthSummary.setTotalDuration(monthSummary.getTotalDuration() + time));
    }

    private void subtractTimeFromTraining(TrainerWorkload workload, int year, int month, int duration){
        updateTrainingDuration(workload, year, month, duration, (monthSummary, time) ->
                monthSummary.setTotalDuration(monthSummary.getTotalDuration() - time));
    }

    private void updateTrainingDuration(TrainerWorkload workload, int year, int month, int duration,
                                        BiConsumer<TrainingMonthSummary, Integer> operation) {
        TrainingMonthSummary monthSummary = trainingMonthSummaryService
                .getByTrainerIdAndYearAndMonthNumber(workload.getTrainerId(), year, month);

        TrainingYear trainingYear = removeValuesFromWorkload(workload, year, monthSummary);

        operation.accept(monthSummary, duration);

        addValuesToWorkload(workload, trainingYear, monthSummary);
        trainerWorkloadRepository.save(workload);
    }

    private TrainingYear removeValuesFromWorkload(TrainerWorkload workload, int year, TrainingMonthSummary monthSummary){
        TrainingYear trainingYear = findYearByNumber(workload.getTrainingYears(), year).get();
        workload.getTrainingYears().remove(trainingYear);
        trainingYear.getMonths().remove(monthSummary);

        return trainingYear;
    }

    private void addValuesToWorkload(TrainerWorkload workload, TrainingYear trainingYear, TrainingMonthSummary monthSummary){
        trainingYear.getMonths().add(monthSummary);
        workload.getTrainingYears().add(trainingYear);
    }

    private boolean isMonthNumberInYearExist(TrainerWorkload workload, int year, int monthNumber){
        return isYearExist(workload, year) &&
                isMonthExist(findYearByNumber(workload.getTrainingYears(), year).get().getMonths(), monthNumber);
    }

    private boolean isYearExist(TrainerWorkload workload, int year){
        return findYearByNumber(workload.getTrainingYears(), year).isPresent();
    }

    private boolean isMonthExist(List<TrainingMonthSummary> monthList, int number){
        return findMonthByNumber(monthList, number).isPresent();
    }

    private Optional<TrainingYear> findYearByNumber(List<TrainingYear> yearList, int number) {
        for (TrainingYear year : yearList) {
            if (year.getTrainingYear() == number){
                return Optional.of(year);
            }
        }
        return Optional.empty();
    }

    private Optional<TrainingMonthSummary> findMonthByNumber(List<TrainingMonthSummary> monthList, int number){
        for (TrainingMonthSummary month : monthList) {
            if (month.getMonthNumber() == number){
                return Optional.of(month);
            }
        }
        return Optional.empty();
    }

    private void createNewTrainerWorkload(TrainerWorkloadRequestDTO trainerWorkloadRequestDTO){
        if(trainerWorkloadRequestDTO.getActionType().equals(ActionType.ADD)){
            TrainerWorkload trainerWorkload = TrainerWorkloadRequestDTO.toTrainerWorkload(trainerWorkloadRequestDTO);
            trainerWorkloadRepository.save(trainerWorkload);
        } else {
            throw new IllegalArgumentException("Cannot delete the trainer workload because it doesn't exist");
        }
    }
}
