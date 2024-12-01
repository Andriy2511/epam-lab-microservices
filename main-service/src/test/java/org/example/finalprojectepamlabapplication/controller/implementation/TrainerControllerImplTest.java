package org.example.finalprojectepamlabapplication.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.TrainingService;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerImplTest {

    @InjectMocks
    TrainerControllerImpl trainerController;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingService trainingService;

    UserDTO userDTO;
    TrainingTypeDTO trainingTypeDTO;
    TrainerDTO trainerDTO;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(trainerController);

        userDTO = DTOBuilder.buildUserDTO(1L);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "Test Training Type");
        trainerDTO = DTOBuilder.buildTrainerDTO(1L, userDTO, trainingTypeDTO);

        lenient().when(trainerService.getTrainerByUserId(1L)).thenReturn(trainerDTO);
    }

    @Test
    public void getTrainerTest() throws JsonProcessingException {
        given()
                .when()
                    .get("/trainers/1")
                .then()
                    .statusCode(200)
                    .body(equalTo(toJSON(trainerDTO)));
    }

    @Test
    public void updateTrainerTest() throws JsonProcessingException {
        when(trainerService.updateTrainer(any(TrainerDTO.class))).thenReturn(trainerDTO);

        given()
                .contentType("application/json")
                    .body(toJSON(trainerDTO))
                .when()
                    .put("/trainers/1")
                .then()
                    .statusCode(200)
                .body(equalTo(toJSON(trainerDTO)));
    }

    @Test
    public void getTrainingWorkloadTest() throws JsonProcessingException {
        TrainingMonthSummaryResponseDTO responseDTO = new TrainingMonthSummaryResponseDTO(24, 4);
        ObjectMapper objectMapper = new ObjectMapper();

        when(trainerService.getTrainerWorkload(anyLong(), anyInt(), anyInt())).thenReturn(responseDTO);

        given()
                .when()
                    .get("/trainers/1/workload?year=2000&month=4")
                .then()
                    .statusCode(200)
                    .body(equalTo(objectMapper.writeValueAsString(responseDTO)));
    }

    private String toJSON(TrainerDTO trainerDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(trainerDTO);
    }
}
