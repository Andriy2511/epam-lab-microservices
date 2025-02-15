package org.example.finalprojectepamlabapplication.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.model.Trainer;
import org.example.finalprojectepamlabapplication.repository.TraineeRepository;
import org.example.finalprojectepamlabapplication.model.Trainee;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.service.TraineeService;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TraineeDTO addTrainee(TraineeDTO traineeDTO) {
        String rawPassword = traineeDTO.getUserDTO().getPassword();
        Trainee trainee = TraineeDTO.toEntity(traineeDTO);

        User user = userService.setUsernameAndPasswordForUser(trainee.getUser());
        user.setPassword(passwordEncoder.encode(rawPassword));

        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);

        trainee.getUser().setPassword(rawPassword);

        return buildTraineeDTOWithRawPassword(trainee);
    }

    @Override
    public TraineeDTO updateTrainee(TraineeDTO traineeDTO) {
        Trainee trainee = traineeRepository.findById(traineeDTO.getId()).orElseThrow();
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setAddress(traineeDTO.getAddress());
        trainee.setUser(UserDTO.toEntity(userService.updateUser(traineeDTO.getUserDTO())));
        traineeRepository.save(trainee);
        return TraineeDTO.toDTO(trainee);
    }

    @Override
    public TraineeDTO deleteTrainee(Long id) {
        return TraineeDTO.toDTO(traineeRepository.deleteTraineeById(id).orElseThrow());
    }

    @Override
    public TraineeDTO getTraineeById(Long id) {
        log.info("Searching Trainee with id {}", id);
        Optional<Trainee> trainee = traineeRepository.findById(id);
        if (trainee.isEmpty()){
            log.warn("Trainee with id {} not found.", id);
        }
        return TraineeDTO.toDTO(trainee.orElseThrow());
    }

    @Override
    public TraineeDTO updateTrainersListByTraineeId(List<TrainerDTO> trainerDTOList, Long traineeId){
        Trainee trainee = traineeRepository.findById(traineeId).orElseThrow();
        List<Trainer> newTrainers = trainerDTOList.stream().map(TrainerDTO::toEntity).toList();
        trainee.setTrainers(newTrainers);
        return TraineeDTO.toDTO(traineeRepository.save(trainee));
    }

    @Override
    public TraineeDTO getTraineeByUserId(Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return userDTO.getTraineeDTO();
    }

    @Override
    public TraineeDTO getTraineeByUserUsername(String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        return userDTO.getTraineeDTO();
    }

    private TraineeDTO buildTraineeDTOWithRawPassword(Trainee trainee) {
        TraineeDTO traineeDTO = TraineeDTO.toDTO(trainee);
        String rawPassword = trainee.getUser().getPassword();
        return traineeDTO.toBuilder()
                .userDTO(traineeDTO.getUserDTO().toBuilder()
                        .password(rawPassword)
                        .build())
                .build();
    }
}
