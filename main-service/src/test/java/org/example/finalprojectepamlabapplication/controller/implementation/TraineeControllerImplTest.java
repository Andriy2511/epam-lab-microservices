package org.example.finalprojectepamlabapplication.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerImplTest {

    @InjectMocks
    private TraineeControllerImpl traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private UserService userService;

    private TraineeDTO traineeDTO;
    private UserDTO firstUserDTO;
    private UserDTO secondUserDTO;
    private TrainingTypeDTO trainingTypeDTO;
    private TrainerDTO trainerDTO;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(traineeController);

        firstUserDTO = DTOBuilder.buildUserDTO(1L);
        secondUserDTO = DTOBuilder.buildUserDTO(2L);
        traineeDTO = DTOBuilder.buildTraineeDTO(1L, firstUserDTO);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "Test Training Type");
        trainerDTO = DTOBuilder.buildTrainerDTO(1L, secondUserDTO, trainingTypeDTO);

        lenient().when(traineeService.getTraineeByUserId(anyLong())).thenReturn(traineeDTO);
    }

    @Test
    public void getTraineeByIdTest() throws JsonProcessingException {
        given()
                .when()
                    .get("/trainees/1")
                .then()
                    .statusCode(200)
                    .body(equalTo(toJSON(traineeDTO)));
    }

    @Test
    public void deleteTraineeTest(){
        given()
                .when()
                    .delete("/trainees/1")
                .then()
                    .statusCode(200);
    }

    @Test
    public void getTrainersNotAssignedToTraineeTest(){
        List<TrainerDTO> trainerDTOList = new ArrayList<>();
        trainerDTOList.add(trainerDTO);

        when(trainerService.getTrainersNotAssignedToTrainee(anyLong())).thenReturn(trainerDTOList);

        given()
                .when()
                    .get("/trainees/1/not-assigned-trainers")
                .then()
                    .statusCode(200)
                    .body("size()", is(1));
    }

    private String toJSON(TraineeDTO traineeDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(traineeDTO);
    }
}
