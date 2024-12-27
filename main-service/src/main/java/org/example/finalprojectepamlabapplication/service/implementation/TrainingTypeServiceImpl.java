package org.example.finalprojectepamlabapplication.service.implementation;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.model.TrainingType;
import org.example.finalprojectepamlabapplication.repository.TrainingTypeRepository;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public TrainingTypeDTO getTrainingTypeByName(String name) {
        Optional<TrainingType> trainingType = trainingTypeRepository.findByTrainingTypeName(name);
        return TrainingTypeDTO.toDTO(trainingType.orElseThrow());
    }

    @Override
    public List<TrainingTypeDTO> getAllTrainingTypes() {
        return trainingTypeRepository.findAll().stream().map(TrainingTypeDTO::toDTO).toList();
    }

    @Override
    public TrainingTypeDTO addNewTrainingTypeByName(String name) {
        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setTrainingTypeName(name);
        return TrainingTypeDTO.toDTO(trainingTypeRepository.save(newTrainingType));
    }

    @Override
    public TrainingTypeDTO getOrCreateTrainingTypeByName(String name){
        try {
            return getTrainingTypeByName(name);
        } catch (NoSuchElementException e) {
            return addNewTrainingTypeByName(name);
        }
    }
}
