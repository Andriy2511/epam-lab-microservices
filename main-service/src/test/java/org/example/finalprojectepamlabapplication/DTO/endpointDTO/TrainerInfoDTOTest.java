package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainerInfoDTOTest {

    private UserDTO userDTO;
    private TrainingTypeDTO trainingTypeDTO;
    private TrainerDTO trainerDTO;

    @BeforeEach
    public void setUp() {
        userDTO = DTOBuilder.buildUserDTO(1L);
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "test training type");
        trainerDTO = DTOBuilder.buildTrainerDTO(1L, userDTO, trainingTypeDTO);
    }

    @Test
    public void toTrainerInfoDTOTest(){
        TrainerInfoDTO trainerInfoDTO = TrainerInfoDTO.toTrainerInfoDTO(trainerDTO);

        Assertions.assertEquals(trainerDTO.getId(), trainerInfoDTO.getId());
        Assertions.assertEquals(trainerDTO.getUserDTO().getUsername(), trainerInfoDTO.getUsername());
        Assertions.assertEquals(trainerDTO.getUserDTO().getFirstName(), trainerInfoDTO.getFirstName());
        Assertions.assertEquals(trainerDTO.getUserDTO().getLastName(), trainerInfoDTO.getLastName());
        Assertions.assertEquals(trainerDTO.getTrainingTypeDTO(), trainerInfoDTO.getSpecialization());

    }
}
