package org.example.trainerworkloadservice.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.example.trainerworkloadservice.service.TrainingMonthSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadControllerImplTest {

    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @Mock
    private TrainingMonthSummaryService trainingMonthSummaryService;

    @InjectMocks
    private TrainerWorkloadControllerImpl trainerWorkloadController;

    private TrainerWorkloadRequestDTO trainerWorkloadRequestDTO;
    private TrainingMonthSummary trainingMonthSummary;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(trainerWorkloadController);

        trainerWorkloadRequestDTO = new TrainerWorkloadRequestDTO();
        trainerWorkloadRequestDTO.setTrainerId(1L);
        trainerWorkloadRequestDTO.setUsername("trainer_user");
        trainerWorkloadRequestDTO.setFirstName("John");
        trainerWorkloadRequestDTO.setLastName("Doe");
        trainerWorkloadRequestDTO.setActive(true);
        trainerWorkloadRequestDTO.setDate(new Date());
        trainerWorkloadRequestDTO.setTrainingDuration(60);
        trainerWorkloadRequestDTO.setActionType(ActionType.ADD);

        trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(6);
        trainingMonthSummary.setTotalDuration(15);
    }

    @Test
    public void updateTimeForTrainerTest() throws JsonProcessingException {
        doNothing().when(trainerWorkloadService).updateTimeForTrainerWorkload(any(TrainerWorkloadRequestDTO.class));

        given()
                .contentType("application/json")
                .body(toJSON(trainerWorkloadRequestDTO))
                .when()
                .patch("/trainer-workload/training-time")
                .then()
                .statusCode(200);

        verify(trainerWorkloadService, times(1)).updateTimeForTrainerWorkload(any(TrainerWorkloadRequestDTO.class));
    }

    @Test
    public void getMonthSummaryByTrainerAndYearAndMonthTest() throws JsonProcessingException {
        when(trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(1L, 2023, 6))
                .thenReturn(trainingMonthSummary);

        given()
                .when()
                .get("/trainer-workload/1?training-year=2023&month-number=6")
                .then()
                .statusCode(200)
                .body(equalTo(toJSON(trainingMonthSummary)));
    }

    private String toJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
