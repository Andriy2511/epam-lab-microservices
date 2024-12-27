package org.example.finalprojectepamlabapplication.controller.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.*;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.model.Trainer;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.TrainingService;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    private TrainingDTO trainingDTO;

    UserDTO userDTO;
    UserDTO secondUserDTO;
    TraineeDTO traineeDTO;
    TrainingTypeDTO trainingTypeDTO;
    TrainerDTO trainerDTO;


    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(trainerController);

        userDTO = DTOBuilder.buildUserDTO(1L);
        secondUserDTO = DTOBuilder.buildUserDTO(2L);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "Test Training Type");
        trainerDTO = DTOBuilder.buildTrainerDTO(1L, userDTO, trainingTypeDTO);
        traineeDTO = DTOBuilder.buildTraineeDTO(1L, secondUserDTO);
        trainingDTO = DTOBuilder.buildTrainingDTO(1L, traineeDTO, trainerDTO, trainingTypeDTO);

        lenient().when(trainerService.getTrainerByUserId(1L)).thenReturn(trainerDTO);
    }

    @Test
    public void getTrainerTest() {
        GumUserDetails userDetails = mock(GumUserDetails.class);
        when(userDetails.getUsername()).thenReturn("Test.User");
        when(trainerService.getTrainerByUsername("Test.User")).thenReturn(trainerDTO);

        TrainerDTO result = trainerController.getTrainer(userDetails);

        assertNotNull(result);
        assertEquals("Test.User", result.getUserDTO().getUsername());
        verify(trainerService, times(1)).getTrainerByUsername("Test.User");
    }

    @Test
    public void updateTrainerTest() {
        GumUserDetails userDetails = mock(GumUserDetails.class);
        when(userDetails.getUsername()).thenReturn("Test.User");
        when(trainerService.getTrainerByUsername("Test.User")).thenReturn(trainerDTO);
        when(trainerService.updateTrainer(any(TrainerDTO.class))).thenReturn(trainerDTO);

        TrainerDTO updatedTrainer = trainerController.updateTrainer(userDetails, trainerDTO);

        assertNotNull(updatedTrainer);
        assertEquals(1L, updatedTrainer.getId());
        verify(trainerService, times(1)).updateTrainer(any(TrainerDTO.class));
    }

    @Test
    public void getTrainingWorkloadTest() {
        GumUserDetails userDetails = mock(GumUserDetails.class);
        when(userDetails.getUsername()).thenReturn("Test.User");
        when(trainerService.getTrainerByUsername("Test.User")).thenReturn(trainerDTO);

        TrainingMonthSummaryResponseDTO workload = new TrainingMonthSummaryResponseDTO();
        when(trainerService.getTrainerWorkload(1L, 2024, 12)).thenReturn(workload);

        TrainingMonthSummaryResponseDTO result = trainerController.getTrainingWorkload(userDetails, 2024, 12);

        assertNotNull(result);
        verify(trainerService, times(1)).getTrainerWorkload(1L, 2024, 12);
    }
}
