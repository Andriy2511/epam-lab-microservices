package org.example.trainerworkloadservice.consumer;

import org.example.trainerworkloadservice.DTO.GetTrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.filter.TransactionLoggingFilter;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadConsumerTest {

    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @Mock
    private TrainingMonthSummaryService trainingMonthSummaryService;

    @InjectMocks
    private TrainerWorkloadConsumer trainerWorkloadConsumer;

    private TrainerWorkloadRequestDTO trainerWorkloadRequestDTO;
    private GetTrainerWorkloadRequestDTO request;
    private TrainingMonthSummary trainingMonthSummary;

    @BeforeEach
    void setUp() {
        trainerWorkloadRequestDTO = initializeTrainerWorkloadRequestDTO();
        request = initializeGetTrainerWorkloadRequestDTO();
        trainingMonthSummary = initializeTrainingMonthSummary();
    }

    @Test
    public void receiveMessageUpdateWorkloadTest(){
        trainerWorkloadConsumer.receiveMessageUpdateWorkload(trainerWorkloadRequestDTO);
        verify(trainerWorkloadService, times(1)).updateTimeForTrainerWorkload(trainerWorkloadRequestDTO);
    }

    @Test
    public void receiveMessageGetTrainerWorkloadTest(){
        when(trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(
                        request.getTrainerId(), request.getTrainingYear(), request.getMonthNumber())).thenReturn(trainingMonthSummary);

        trainerWorkloadConsumer.receiveMessageGetTrainerWorkload(request);

        assertEquals(trainerWorkloadConsumer.receiveMessageGetTrainerWorkload(request), trainingMonthSummary);
    }

    private TrainerWorkloadRequestDTO initializeTrainerWorkloadRequestDTO(){
        TrainerWorkloadRequestDTO trainerWorkload = new TrainerWorkloadRequestDTO();
        trainerWorkload.setTrainerId(1L);
        trainerWorkload.setUsername("Test");
        trainerWorkload.setFirstName("Test");
        trainerWorkload.setLastName("Test");
        trainerWorkload.setActive(true);
        trainerWorkload.setActionType(ActionType.ADD);
        return trainerWorkload;
    }

    private GetTrainerWorkloadRequestDTO initializeGetTrainerWorkloadRequestDTO(){
        GetTrainerWorkloadRequestDTO requestDTO = new GetTrainerWorkloadRequestDTO();
        requestDTO.setTrainerId(1L);
        requestDTO.setTrainingYear(2024);
        requestDTO.setMonthNumber(12);
        return requestDTO;
    }

    private TrainingMonthSummary initializeTrainingMonthSummary(){
        TrainingMonthSummary trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(1);
        trainingMonthSummary.setTotalDuration(60);
        return trainingMonthSummary;
    }
}
