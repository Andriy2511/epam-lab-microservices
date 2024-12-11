package org.example.finalprojectepamlabapplication.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.messenger.TrainerWorkloadMessengerClient;
import org.example.finalprojectepamlabapplication.repository.TrainerRepository;
import org.example.finalprojectepamlabapplication.model.Trainer;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.service.TrainerService;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainerWorkloadMessengerClient messengerClient;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService, TrainerWorkloadMessengerClient messengerClient) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.messengerClient = messengerClient;
    }

    @Override
    public TrainerDTO addTrainer(TrainerDTO trainerDTO) {
        Trainer trainer = TrainerDTO.toEntity(trainerDTO);
        User user = userService.setUsernameAndPasswordForUser(trainer.getUser());
        trainer.setUser(user);
        return TrainerDTO.toDTO(trainerRepository.save(trainer));
    }

    @Override
    public TrainerDTO updateTrainer(TrainerDTO trainerDTO) {
        Trainer trainer = trainerRepository.findById(trainerDTO.getId()).orElseThrow();
        trainer.setTrainingType(TrainingTypeDTO.toEntity(trainerDTO.getTrainingTypeDTO()));
        trainer.setUser(UserDTO.toEntity(userService.updateUser(trainerDTO.getUserDTO())));
        return TrainerDTO.toDTO(trainerRepository.save(trainer));
    }

    @Override
    public TrainerDTO deleteTrainer(Long id) {
        return TrainerDTO.toDTO(trainerRepository.deleteTrainerById(id).orElseThrow());
    }

    @Override
    public TrainerDTO getTrainerById(Long id) {
        log.info("Searching Trainer with id {}", id);
        Optional<Trainer> trainer = trainerRepository.findById(id);
        if (trainer.isEmpty()){
            log.warn("Trainer with id {} not found.", id);
        }

        return TrainerDTO.toDTO(trainer.orElseThrow());
    }

    @Override
    public List<TrainerDTO> getTrainersNotAssignedToTrainee(Long id) {
        List<Trainer> trainers = trainerRepository.findTrainersWhichNotAssignToTraineeByUserId(id);
        return trainers.stream().map(TrainerDTO::toDTO).toList();
    }

    @Override
    public TrainerDTO getTrainerByUserId(Long userId){
        UserDTO userDTO = userService.getUserById(userId);
        return userDTO.getTrainerDTO();
    }

    @Override
    public TrainingMonthSummaryResponseDTO getTrainerWorkload(Long id, int year, int month){
        return messengerClient.getTrainingWorkload(id, year, month);
    }
}
