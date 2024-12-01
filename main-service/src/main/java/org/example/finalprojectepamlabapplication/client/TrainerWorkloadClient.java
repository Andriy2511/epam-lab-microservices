package org.example.finalprojectepamlabapplication.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ActionType;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerWorkloadRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingDTO;
import org.example.finalprojectepamlabapplication.exception.TrainerWorkloadException;
import org.example.finalprojectepamlabapplication.exception.TrainerWorkloadTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class TrainerWorkloadClient {

    private final WebClient webClient;

    @Autowired
    public TrainerWorkloadClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clone()
                .baseUrl("http://trainer-workload-service")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(5))))
                .build();
    }

    @CircuitBreaker(
            name = "trainerWorkloadService",
            fallbackMethod = "fallbackUpdateTrainerWorkload"
    )
    public void updateTrainerWorkload(TrainingDTO trainingDTO, ActionType actionType) {
        try {
            webClient.patch()
                    .uri(uriBuilder -> uriBuilder.path("/trainer-workload/training-time").build())
                    .bodyValue(TrainerWorkloadRequestDTO.toTrainerWorkloadRequestDtoFromTrainingDto(trainingDTO, actionType))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> handleTimeout(throwable, "updating workload for trainer ID: " + trainingDTO.getTrainer().getId()))
                    .block();
        } catch (WebClientResponseException e) {
            log.debug("Failed to update workload for trainer with ID: {}. Exception info: {}", trainingDTO.getTrainer().getId(), e.getMessage());
            throw new TrainerWorkloadException("Failed to update workload for trainer with ID: " + trainingDTO.getTrainer().getId());
        }
    }

    @SuppressWarnings("unused")
    private void fallbackUpdateTrainerWorkload(TrainingDTO trainingDTO, ActionType actionType, Throwable throwable) {
        log.error("Failed to update workload for trainer ID: {}. Fallback triggered.", trainingDTO.getTrainer().getId(), throwable);
    }

    @CircuitBreaker(
            name = "trainerWorkloadService",
            fallbackMethod = "fallbackGetMonthSummary"
    )
    public TrainingMonthSummaryResponseDTO getMonthSummary(Long trainerId, int trainingYear, int monthNumber) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trainer-workload/{trainerId}")
                            .queryParam("training-year", trainingYear)
                            .queryParam("month-number", monthNumber)
                            .build(trainerId))
                    .retrieve()
                    .bodyToMono(TrainingMonthSummaryResponseDTO.class)
                    .doOnError(throwable -> handleTimeout(throwable,
                            "fetching month summary for trainer ID: " + trainerId + " in year: " + trainingYear + ", month: " + monthNumber))
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Failed to fetch month summary for trainer ID: {} in year: {}, month: {}. Exception: {}",
                    trainerId, trainingYear, monthNumber, e.getMessage());
            throw new TrainerWorkloadException("Failed to fetch month summary for trainer ID: " + trainerId);
        }
    }

    @SuppressWarnings("unused")
    private TrainingMonthSummaryResponseDTO fallbackGetMonthSummary(Long trainerId, int trainingYear, int monthNumber, Throwable throwable) {
        log.error("Fallback triggered for getMonthSummary. Trainer ID: {}, Year: {}, Month: {}. Exception: {}",
                trainerId, trainingYear, monthNumber, throwable.getMessage());
        return new TrainingMonthSummaryResponseDTO();
    }

    private void handleTimeout(Throwable throwable, String actionDescription) {
        if (throwable.getCause() instanceof TimeoutException) {
            log.error("Timeout occurred during: {}", actionDescription);
            throw new TrainerWorkloadTimeoutException("Timeout occurred during: " + actionDescription);
        }
    }
}
