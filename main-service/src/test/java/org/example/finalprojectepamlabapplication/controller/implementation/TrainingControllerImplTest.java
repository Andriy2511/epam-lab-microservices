package org.example.finalprojectepamlabapplication.controller.implementation;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.*;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerImplTest {

    @InjectMocks
    TrainingControllerImpl trainingController;

    @Mock
    TrainingService trainingService;

    @Mock
    TraineeService traineeService;

    @Mock
    TrainerService trainerService;

    @Mock
    UserService userService;

    @Mock
    TrainingTypeService trainingTypeService;

    private UserDTO firstUserDTO;
    private UserDTO secondUserDTO;
    private TraineeDTO traineeDTO;
    private TrainerDTO trainerDTO;
    private TrainingTypeDTO trainingTypeDTO;
    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(trainingController);

        firstUserDTO = DTOBuilder.buildUserDTO(1L);
        secondUserDTO = DTOBuilder.buildUserDTO(2L);
        traineeDTO = DTOBuilder.buildTraineeDTO(1L, firstUserDTO);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "TestTrainingType");
        trainerDTO = DTOBuilder.buildTrainerDTO(2L, secondUserDTO, trainingTypeDTO);
        trainingDTO = DTOBuilder.buildTrainingDTO(1L, traineeDTO, trainerDTO, trainingTypeDTO);
    }

    @Test
    public void addTrainingTest(){
        when(traineeService.getTraineeByUserId(anyLong())).thenReturn(traineeDTO);
        when(userService.getUserByUsername(anyString())).thenReturn(secondUserDTO);
        when(trainerService.getTrainerById(anyLong())).thenReturn(trainerDTO);
        when(trainingTypeService.getTrainingTypeByName(anyString())).thenReturn(trainingTypeDTO);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date());

        given()
                .formParam("trainerUsername", "testTrainerUsername")
                .formParam("trainingName", "Test Training")
                .formParam("trainingDate", formattedDate)
                .formParam("trainingTypeName", "Test Training Type")
                .formParam("trainingDuration", 60)
                .when()
                    .post("/trainings/1")
                .then()
                    .statusCode(200);
    }

    @Test
    public void cancelTrainingTest(){
        doNothing().when(trainingService).deleteTrainingById(anyLong());

        given().when().delete("/trainings/1").then().statusCode(200);
        verify(trainingService, times(1)).deleteTrainingById(anyLong());
    }
}
