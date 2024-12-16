package org.example.trainerworkloadservice.service.implementation;

import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainerWorkloadRepository;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.example.trainerworkloadservice.utility.DateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @Mock
    private TrainingMonthSummaryService trainingMonthSummaryService;

    @InjectMocks
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    private TrainerWorkloadRequestDTO workloadRequestDTO;
    private TrainerWorkload trainerWorkload;
    private TrainingYear trainingYear;
    private TrainingMonthSummary trainingMonthSummary;

    @BeforeEach
    void setUp() {
        initializeTrainingMonthSummary();
        initializeTrainingYear();
        initializeTrainerWorkload();
        initializeTrainerWorkloadRequestDTO();
    }

    @Test
    void testUpdateTimeForTrainerWorkloadWithActionTypeAdd() {
        when(trainerWorkloadRepository.findByTrainerId(100L)).thenReturn(Optional.of(trainerWorkload));
        when(trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(anyLong(), anyInt(), anyInt()))
                .thenReturn(trainingMonthSummary);

        trainerWorkloadService.updateTimeForTrainerWorkload(workloadRequestDTO);

        assertEquals(15, trainingMonthSummary.getTotalDuration());
    }

    @Test
    void testUpdateTimeForTrainerWorkloadWithActionTypeDelete() {
        when(trainerWorkloadRepository.findByTrainerId(100L)).thenReturn(Optional.of(trainerWorkload));
        when(trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(anyLong(), anyInt(), anyInt()))
                .thenReturn(trainingMonthSummary);

        workloadRequestDTO.setActionType(ActionType.DELETE);
        trainerWorkloadService.updateTimeForTrainerWorkload(workloadRequestDTO);

        assertEquals(5, trainingMonthSummary.getTotalDuration());
    }

    private void initializeTrainerWorkload() {
        trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerId(100L);
        trainerWorkload.setUsername("Test.Trainer");
        trainerWorkload.setTrainingYears(new ArrayList<>());
        trainerWorkload.getTrainingYears().add(trainingYear);
    }

    private void initializeTrainerWorkloadRequestDTO() {
        workloadRequestDTO = new TrainerWorkloadRequestDTO();
        workloadRequestDTO.setTrainerId(100L);
        workloadRequestDTO.setDate(new Date());
        workloadRequestDTO.setActionType(ActionType.ADD);
        workloadRequestDTO.setTrainingDuration(5);
    }

    private void initializeTrainingYear(){
        trainingYear = new TrainingYear();
        trainingYear.setTrainingYear(DateConverter.getYearAsInteger(new Date()));
        trainingYear.setMonths(new ArrayList<>());
        trainingYear.getMonths().add(trainingMonthSummary);
    }

    private void initializeTrainingMonthSummary(){
        trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(DateConverter.getMonthAsInteger(new Date()));
        trainingMonthSummary.setTotalDuration(10);
    }
}
