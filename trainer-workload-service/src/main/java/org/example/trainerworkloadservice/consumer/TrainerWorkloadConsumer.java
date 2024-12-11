package org.example.trainerworkloadservice.consumer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.trainerworkloadservice.DTO.GetTrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
public class TrainerWorkloadConsumer {

    private final TrainerWorkloadService trainerWorkloadService;
    private final TrainingMonthSummaryService trainingMonthSummaryService;

    @Autowired
    public TrainerWorkloadConsumer(TrainerWorkloadService trainerWorkloadService, TrainingMonthSummaryService trainingMonthSummaryService) {
        this.trainerWorkloadService = trainerWorkloadService;
        this.trainingMonthSummaryService = trainingMonthSummaryService;
    }

    @JmsListener(destination = "${messaging.queues.trainerWorkload}")
    public void receiveMessageUpdateWorkload(@Valid TrainerWorkloadRequestDTO trainerWorkloadRequestDTO) {
        trainerWorkloadService.updateTimeForTrainerWorkload(trainerWorkloadRequestDTO);
    }

    @JmsListener(destination = "${messaging.queues.getTrainerWorkloadRequest}")
    @SendTo(value = "${messaging.queues.getTrainerWorkloadResponse}")
    public TrainingMonthSummary receiveMessageGetTrainerWorkload(GetTrainerWorkloadRequestDTO request){
        return trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(
                request.getTrainerId(), request.getTrainingYear(), request.getMonthNumber()
        );
    }
}
