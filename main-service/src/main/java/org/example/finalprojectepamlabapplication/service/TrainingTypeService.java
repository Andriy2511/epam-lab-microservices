package org.example.finalprojectepamlabapplication.service;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {
    TrainingTypeDTO getTrainingTypeByName(String name);

    List<TrainingTypeDTO> getAllTrainingTypes();
}
