package org.example.trainerworkloadservice.service.implementation;

import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainingYearRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingYearServiceImplTest {

    @Mock
    private TrainingYearRepository trainingYearRepository;

    @InjectMocks
    TrainingYearServiceImpl trainingYearService;

    private TrainerWorkload trainerWorkload;
    private TrainingYear trainingYear;

    @BeforeEach
    void setUp() {
        trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerId(101L);

        trainingYear = new TrainingYear();
        trainingYear.setTrainingYear(2023);
        trainingYear.setTrainerWorkload(trainerWorkload);
    }

    @Test
    void testGetByTrainingYearAndTrainerWorkload() {
        when(trainingYearRepository.findByTrainingYearAndTrainerWorkload(2023, trainerWorkload))
                .thenReturn(Optional.of(trainingYear));

        Optional<TrainingYear> result = trainingYearService.getByTrainingYearAndTrainerWorkload(2023, trainerWorkload);

        assertTrue(result.isPresent());
        assertEquals(2023, result.get().getTrainingYear());
    }

    @Test
    void testGetByTrainingYearAndTrainerWorkloadWhenResultNotFound() {
        when(trainingYearRepository.findByTrainingYearAndTrainerWorkload(2024, trainerWorkload))
                .thenReturn(Optional.empty());

        Optional<TrainingYear> result = trainingYearService.getByTrainingYearAndTrainerWorkload(2024, trainerWorkload);

        assertFalse(result.isPresent());
    }

    @Test
    void testAddTrainingYear() {
        when(trainingYearRepository.save(trainingYear)).thenReturn(trainingYear);

        TrainingYear result = trainingYearService.addTrainingYear(trainingYear);

        assertNotNull(result);
        assertEquals(2023, result.getTrainingYear());
    }
}
