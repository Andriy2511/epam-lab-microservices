package org.example.finalprojectepamlabapplication.messenger;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ActionType;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerWorkloadRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadMessengerProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${messaging.queues.trainerWorkload}")
    private String traineeWorkloadQueue;

    @Autowired
    public TrainerWorkloadMessengerProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendUpdateTrainerWorkload(TrainingDTO trainingDTO, ActionType actionType) {
        jmsTemplate.convertAndSend(traineeWorkloadQueue,
                TrainerWorkloadRequestDTO.toTrainerWorkloadRequestDtoFromTrainingDto(trainingDTO, actionType));
    }
}
