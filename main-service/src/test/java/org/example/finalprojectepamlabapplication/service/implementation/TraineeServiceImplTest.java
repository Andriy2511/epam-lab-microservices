package org.example.finalprojectepamlabapplication.service.implementation;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.model.Trainee;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.repository.TraineeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private User user;
    private Trainee trainee;
    private TraineeDTO traineeDTO;
    private TrainingTypeDTO trainingTypeDTO;
    private List<TrainerDTO> trainers;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = createUser();
        trainee = createTrainee(user);
        traineeDTO = createTraineeDTO();
        trainingTypeDTO = createTrainingTypeDTO();
        trainers = createTrainerDTOList();
    }

    @Test
    public void testAddTrainee() {
        when(userService.getAllUsers()).thenReturn(List.of());
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDTO result = traineeService.addTrainee(traineeDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("first.user", result.getUserDTO().getUsername());
    }

    @Test
    public void testUpdateTrainee() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(trainee));
        when(userService.updateUser(any(UserDTO.class))).thenReturn(traineeDTO.getUserDTO());
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.addTrainee(traineeDTO);

        Date oldDate = traineeDTO.getDateOfBirth();
        LocalDate localDate = LocalDate.of(2015, 1, 1);
        Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        traineeDTO = traineeDTO.toBuilder().dateOfBirth(newDate).build();
        TraineeDTO result = traineeService.updateTrainee(traineeDTO);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(oldDate, result.getDateOfBirth());
    }

    @Test
    public void testDeleteTrainee() {
        when(traineeRepository.deleteTraineeById(anyLong())).thenReturn(Optional.of(trainee));

        TraineeDTO result = traineeService.deleteTrainee(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(trainee.getId(), result.getId());
    }

    @Test
    public void testGetTraineeById() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(trainee));

        TraineeDTO result = traineeService.getTraineeById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(trainee, TraineeDTO.toEntity(result));
    }

    @Test
    public void testUpdateTrainersListByTraineeId() {
        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDTO result = traineeService.updateTrainersListByTraineeId(trainers, 1L);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetTraineeByUserId(){
        UserDTO userDTO = mock(UserDTO.class);
        TraineeDTO traineeDTO = mock(TraineeDTO.class);

        when(userService.getUserById(anyLong())).thenReturn(userDTO);
        when(userDTO.getTraineeDTO()).thenReturn(traineeDTO);

        TraineeDTO result = traineeService.getTraineeByUserId(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(traineeDTO, result);
    }

    private Trainee createTrainee(User user) {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("Address");
        trainee.setUser(user);
        return trainee;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("first");
        user.setLastName("user");
        user.setUsername("first.user");
        user.setPassword("password123");
        return user;
    }

    private UserDTO createUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .firstName("first")
                .lastName("user")
                .username("first.user")
                .password("password123")
                .build();
    }

    private TraineeDTO createTraineeDTO() {
        return TraineeDTO.builder()
                .id(1L)
                .dateOfBirth(new Date())
                .address("Address")
                .userDTO(createUserDTO())
                .build();
    }

    private TrainingTypeDTO createTrainingTypeDTO() {
        return TrainingTypeDTO.builder()
                .id(1L)
                .trainingTypeName("Strength Training")
                .build();
    }

    private List<TrainerDTO> createTrainerDTOList() {
        UserDTO trainerUserDTO = UserDTO.builder()
                .id(2L)
                .firstName("second")
                .lastName("user")
                .username("second.user")
                .password("1234567891")
                .build();

        TrainerDTO trainerDTO = TrainerDTO.builder()
                .id(1L)
                .trainingTypeDTO(trainingTypeDTO)
                .userDTO(trainerUserDTO)
                .build();

        return List.of(trainerDTO);
    }
}
