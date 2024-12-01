package org.example.trainerworkloadservice.service.implementation;

import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainerWorkloadRepository;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.example.trainerworkloadservice.service.TrainingYearService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private TrainingYearService trainingYearService;

    @InjectMocks
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    private TrainerWorkloadRequestDTO workloadRequestDTO;
    private TrainerWorkload trainerWorkload;
    private TrainingYear trainingYear;
    private TrainingMonthSummary trainingMonthSummary;

    @BeforeEach
    void setUp() {
        initializeTrainerWorkload();
    }

    @Test
    void testUpdateTimeForTrainerWorkloadWithActionTypeAdd() {
        when(trainerWorkloadRepository.findByTrainerId(100L)).thenReturn(Optional.of(trainerWorkload));
        when(trainingYearService.getByTrainingYearAndTrainerWorkload(anyInt(), eq(trainerWorkload)))
                .thenReturn(Optional.of(trainingYear));
        when(trainingMonthSummaryService.getMonthByMonthNumberAndTrainingYear(anyInt(), eq(trainingYear)))
                .thenReturn(Optional.of(trainingMonthSummary));

        trainerWorkloadService.updateTimeForTrainerWorkload(workloadRequestDTO);

        verify(trainingMonthSummaryService, times(1)).updateTrainingMonthSummary(any());
        assertEquals(15, trainingMonthSummary.getTotalDuration());
    }

    @Test
    void testUpdateTimeForTrainerWorkloadWithActionTypeDelete() {
        when(trainerWorkloadRepository.findByTrainerId(100L)).thenReturn(Optional.of(trainerWorkload));
        when(trainingYearService.getByTrainingYearAndTrainerWorkload(anyInt(), eq(trainerWorkload)))
                .thenReturn(Optional.of(trainingYear));
        when(trainingMonthSummaryService.getMonthByMonthNumberAndTrainingYear(anyInt(), eq(trainingYear)))
                .thenReturn(Optional.of(trainingMonthSummary));

        workloadRequestDTO.setActionType(ActionType.DELETE);
        trainerWorkloadService.updateTimeForTrainerWorkload(workloadRequestDTO);

        verify(trainingMonthSummaryService, times(1)).updateTrainingMonthSummary(any());
        assertEquals(5, trainingMonthSummary.getTotalDuration());
    }

    @Test
    void testAddTrainerWorkload() {
        when(trainerWorkloadRepository.save(any())).thenReturn(trainerWorkload);

        TrainerWorkload result = trainerWorkloadService.addTrainerWorkload(workloadRequestDTO);

        assertNotNull(result);
        assertEquals(100L, result.getTrainerId());
    }

    private void initializeTrainerWorkload() {
        trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerId(100L);

        trainingYear = new TrainingYear();
        trainingYear.setTrainingYear(2023);
        trainingYear.setTrainerWorkload(trainerWorkload);

        trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(6);
        trainingMonthSummary.setTotalDuration(10);
        trainingMonthSummary.setTrainingYear(trainingYear);

        workloadRequestDTO = new TrainerWorkloadRequestDTO();
        workloadRequestDTO.setTrainerId(100L);
        workloadRequestDTO.setDate(new Date());
        workloadRequestDTO.setActionType(ActionType.ADD);
        workloadRequestDTO.setTrainingDuration(5);
    }
}
