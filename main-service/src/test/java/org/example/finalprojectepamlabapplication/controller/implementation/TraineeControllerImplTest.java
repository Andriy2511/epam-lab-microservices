package org.example.finalprojectepamlabapplication.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainerInfoDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.*;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private TrainingDTO trainingDTO;
    private TrainingTypeDTO trainingTypeDTO;
    private TrainerDTO trainerDTO;
    private GumUserDetails userDetails;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(traineeController);

        initializeValues();

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
    public void getTraineeTest() {
        when(traineeService.getTraineeByUserUsername(userDetails.getUsername())).thenReturn(traineeDTO);

        TraineeDTO dto = traineeService.getTraineeByUserUsername(userDetails.getUsername());

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(traineeDTO, dto);
    }

    @Test
    public void updateTraineeTest() {
        when(traineeService.updateTrainee(any(TraineeDTO.class))).thenReturn(traineeDTO);
        when(traineeService.getTraineeByUserUsername(anyString())).thenReturn(traineeDTO);

        TraineeDTO result = traineeController.updateTrainee(userDetails, traineeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(traineeService, times(1)).updateTrainee(any(TraineeDTO.class));
    }

    @Test
    public void deleteTraineeTest() {
        when(traineeService.getTraineeByUserUsername(anyString())).thenReturn(traineeDTO);
        when(traineeService.deleteTrainee(anyLong())).thenReturn(traineeDTO);

        traineeController.deleteTrainee(userDetails);

        verify(traineeService, times(1)).deleteTrainee(1L);
    }

    @Test
    public void getTrainersNotAssignedToTraineeTest() {
        when(traineeService.getTraineeByUserUsername(anyString())).thenReturn(traineeDTO);
        when(trainerService.getTrainersNotAssignedToTrainee(1L)).thenReturn(List.of(trainerDTO));
        when(traineeService.getTraineeByUserUsername(anyString())).thenReturn(traineeDTO);

        List<TrainerDTO> result = traineeController.getTrainersNotAssignedToTrainee(userDetails);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    private String toJSON(TraineeDTO traineeDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(traineeDTO);
    }

    private void initializeValues(){
        firstUserDTO = DTOBuilder.buildUserDTO(1L);
        secondUserDTO = DTOBuilder.buildUserDTO(2L);
        traineeDTO = DTOBuilder.buildTraineeDTO(1L, firstUserDTO);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "Test Training Type");
        trainerDTO = DTOBuilder.buildTrainerDTO(1L, secondUserDTO, trainingTypeDTO);
        trainingDTO = DTOBuilder.buildTrainingDTO(1L, traineeDTO, trainerDTO, trainingTypeDTO);
        initializeUserDetails();
    }

    private void initializeUserDetails(){
        User user = UserDTO.toEntity(traineeDTO.getUserDTO());
        user.setTrainee(TraineeDTO.toEntity(traineeDTO));
        userDetails = new GumUserDetails(user);
    }
}
