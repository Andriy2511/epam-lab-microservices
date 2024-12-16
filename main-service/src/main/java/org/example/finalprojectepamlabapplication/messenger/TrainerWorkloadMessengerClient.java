package org.example.finalprojectepamlabapplication.messenger;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.GetTrainerWorkloadRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.exception.TrainerWorkloadTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TrainerWorkloadMessengerClient {

    private final JmsTemplate jmsTemplate;

    @Value("${messaging.queues.getTrainerWorkloadRequest}")
    private String trainerWorkloadRequest;

    @Value("${messaging.queues.getTrainerWorkloadResponse}")
    private String trainerWorkloadResponse;

    @Autowired
    public TrainerWorkloadMessengerClient(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public TrainingMonthSummaryResponseDTO getTrainingWorkload(Long trainerId, int trainingYear, int monthNumber) {
        GetTrainerWorkloadRequestDTO requestDTO = new GetTrainerWorkloadRequestDTO(trainerId, trainingYear, monthNumber);
        String messageId = generateMessageId();
        jmsTemplate.convertAndSend(trainerWorkloadRequest, requestDTO, message -> {
            message.setStringProperty("messageId", messageId);
            return message;
        });

        return receiveResponse(requestDTO, messageId);
    }

    private TrainingMonthSummaryResponseDTO receiveResponse(GetTrainerWorkloadRequestDTO requestDTO, String messageId) {
        jmsTemplate.setReceiveTimeout(10000);
        String messageSelector = buildMessageSelectorForMassageId(messageId);
        TrainingMonthSummaryResponseDTO responseDTO = (TrainingMonthSummaryResponseDTO) jmsTemplate
                .receiveAndConvert(trainerWorkloadResponse);
        if (responseDTO != null) {
            return responseDTO;
        } else {
            deleteMessageFromQueue(requestDTO, messageSelector);
            throw new TrainerWorkloadTimeoutException("Trainer workload service is not available");
        }
    }

    private void deleteMessageFromQueue(GetTrainerWorkloadRequestDTO requestDTO, String messageSelector) {
        log.warn("Removed message with id {} for trainerId={} from queue", messageSelector, requestDTO.getTrainerId());
        jmsTemplate.receiveSelected(trainerWorkloadRequest, messageSelector);
    }

    private String generateMessageId(){
        return UUID.randomUUID().toString();
    }

    private String buildMessageSelectorForMassageId(String messageId){
        return "messageId = '" + messageId + "'";
    }
}
