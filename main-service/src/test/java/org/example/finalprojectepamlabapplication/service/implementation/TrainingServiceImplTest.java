package org.example.finalprojectepamlabapplication.service.implementation;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ActionType;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.*;
import org.example.finalprojectepamlabapplication.messenger.TrainerWorkloadMessengerProducer;
import org.example.finalprojectepamlabapplication.model.Training;
import org.example.finalprojectepamlabapplication.model.TrainingType;
import org.example.finalprojectepamlabapplication.repository.TrainingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerWorkloadMessengerProducer massagerProducer;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO trainingDTO;
    private TraineeDTO traineeDTO;
    private TrainerDTO trainerDTO;
    private TrainingTypeDTO trainingTypeDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        traineeDTO = createTraineeDTO();
        trainingTypeDTO = createTrainingTypeDTO();
        trainerDTO = createTrainerDTO();
        trainingDTO = createTrainingDTO();

    }

    @Test
    public void testAddTraining() {
        when(trainingRepository.save(any(Training.class))).thenReturn(TrainingDTO.toEntity(trainingDTO));
        doNothing().when(massagerProducer).sendUpdateTrainerWorkload(trainingDTO, ActionType.ADD);

        TrainingDTO result = trainingService.addTraining(trainingDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(trainingDTO.getId(), result.getId());
        Assertions.assertEquals("Morning Yoga", result.getTrainingName());
    }

    @Test
    public void testGetTrainingById() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(training));

        TrainingDTO result = trainingService.getTrainingById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Morning Yoga", result.getTrainingName());
    }

    @Test
    public void testGetTrainingByName() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingByTrainingName(anyString())).thenReturn(Optional.of(training));

        TrainingDTO result = trainingService.getTrainingByName("Morning Yoga");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Morning Yoga", result.getTrainingName());
    }

    @Test
    public void testGetTrainingsByTraineeAndToDate() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTraineeAndCriterion(eq(1L), any(Date.class), isNull(), isNull(), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTraineeAndCriterion(1L, new Date(), null, null, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Morning Yoga", result.get(0).getTrainingName());
    }

    @Test
    public void testGetTrainingsByTraineeAndFromDate() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTraineeAndCriterion(eq(1L), isNull(), any(Date.class), isNull(), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTraineeAndCriterion(1L, null, new Date(), null, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Morning Yoga", result.get(0).getTrainingName());
    }

    @Test
    public void testGetTrainingsByTraineeAndTrainingType() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        TrainingType trainingType = TrainingTypeDTO.toEntity(trainingTypeDTO);
        when(trainingRepository.findTrainingsByTraineeAndCriterion(eq(1L), isNull(), isNull(), eq(trainingType), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTraineeAndCriterion(1L, null, null, trainingType, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Yoga", result.get(0).getTrainingTypeDTO().getTrainingTypeName());
    }

    @Test
    public void testGetTrainingsByTraineeAndTrainerUsername() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTraineeAndCriterion(eq(1L), isNull(), isNull(), isNull(), eq("trainer.username")))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTraineeAndCriterion(1L, null, null, null, "trainer.username");

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("trainer.username", result.get(0).getTrainer().getUserDTO().getUsername());
    }

    @Test
    public void testGetTrainingsByTrainerAndToDate() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTrainerAndCriterion(eq(2L), any(Date.class), isNull(), isNull(), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTrainerAndCriterion(2L, new Date(), null, null, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("trainer.username", result.get(0).getTrainer().getUserDTO().getUsername());
    }

    @Test
    public void testGetTrainingsByTrainerAndFromDate() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTrainerAndCriterion(eq(2L), isNull(), any(Date.class), isNull(), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTrainerAndCriterion(2L, null, new Date(), null, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("trainer.username", result.get(0).getTrainer().getUserDTO().getUsername());
    }

    @Test
    public void testGetTrainingsByTrainerAndTrainingType() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        TrainingType trainingType = TrainingTypeDTO.toEntity(trainingTypeDTO);
        when(trainingRepository.findTrainingsByTrainerAndCriterion(eq(2L), isNull(), isNull(), eq(trainingType), isNull()))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTrainerAndCriterion(2L, null, null, trainingType, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Yoga", result.get(0).getTrainingTypeDTO().getTrainingTypeName());
    }

    @Test
    public void testGetTrainingsByTrainerAndTraineeUsername() {
        Training training = TrainingDTO.toEntity(trainingDTO);
        when(trainingRepository.findTrainingsByTrainerAndCriterion(eq(2L), isNull(), isNull(), isNull(), eq("trainee.username")))
                .thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService
                .getTrainingsByTrainerAndCriterion(2L, null, null, null, "trainee.username");

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("trainee.username", result.get(0).getTrainee().getUserDTO().getUsername());
    }

    private UserDTO createTraineeUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .firstName("TraineeFirst")
                .lastName("TraineeLast")
                .username("trainee.username")
                .build();
    }

    private TraineeDTO createTraineeDTO() {
        return TraineeDTO.builder()
                .id(1L)
                .userDTO(createTraineeUserDTO())
                .dateOfBirth(new Date())
                .address("123 Trainee Street")
                .build();
    }

    private UserDTO createTrainerUserDTO() {
        return UserDTO.builder()
                .id(2L)
                .firstName("TrainerFirst")
                .lastName("TrainerLast")
                .username("trainer.username")
                .build();
    }

    private TrainingTypeDTO createTrainingTypeDTO() {
        return TrainingTypeDTO.builder()
                .id(1L)
                .trainingTypeName("Yoga")
                .build();
    }

    private TrainerDTO createTrainerDTO() {
        return TrainerDTO.builder()
                .id(1L)
                .userDTO(createTrainerUserDTO())
                .trainingTypeDTO(trainingTypeDTO)
                .build();
    }

    private TrainingDTO createTrainingDTO() {
        return TrainingDTO.builder()
                .id(1L)
                .trainee(traineeDTO)
                .trainer(trainerDTO)
                .trainingName("Morning Yoga")
                .trainingTypeDTO(trainingTypeDTO)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
    }
}
