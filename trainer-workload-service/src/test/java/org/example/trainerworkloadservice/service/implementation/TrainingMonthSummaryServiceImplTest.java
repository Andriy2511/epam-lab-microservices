package org.example.trainerworkloadservice.service.implementation;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.model.TrainingYear;
import org.example.trainerworkloadservice.repository.TrainingMonthSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingMonthSummaryServiceImplTest {

    private TrainingYear trainingYear;
    private Long trainerId;
    private Integer year;
    private Integer monthNumber;

    private TrainingMonthSummary trainingMonthSummary;


    @Mock
    private TrainingMonthSummaryRepository trainingMonthSummaryRepository;

    @InjectMocks
    private TrainingMonthSummaryServiceImpl trainingMonthSummaryService;

    @BeforeEach
    void setUp() {
        trainingYear = new TrainingYear();
        trainingYear.setId(1L);

        trainerId = 100L;
        year = 2023;
        monthNumber = 6;

        trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(monthNumber);
    }

    @Test
    void testGetMonthByMonthNumberAndTrainingYear() {
        trainingMonthSummary.setMonthNumber(5);
        trainingMonthSummary.setTrainingYear(trainingYear);

        when(trainingMonthSummaryRepository.findByMonthNumberAndTrainingYear(5, trainingYear))
                .thenReturn(Optional.of(trainingMonthSummary));

        Optional<TrainingMonthSummary> result = trainingMonthSummaryService.getMonthByMonthNumberAndTrainingYear(5, trainingYear);
        assertTrue(result.isPresent());
        assertEquals(5, result.get().getMonthNumber());
    }

    @Test
    void testGetMonthByMonthNumberAndTrainingYearWhenResultNotFound() {
        when(trainingMonthSummaryRepository.findByMonthNumberAndTrainingYear(10, trainingYear))
                .thenReturn(Optional.empty());

        Optional<TrainingMonthSummary> result = trainingMonthSummaryService.getMonthByMonthNumberAndTrainingYear(10, trainingYear);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateTrainingMonthSummary() {
        when(trainingMonthSummaryRepository.save(trainingMonthSummary)).thenReturn(trainingMonthSummary);
        TrainingMonthSummary result = trainingMonthSummaryService.updateTrainingMonthSummary(trainingMonthSummary);

        assertNotNull(result);
        assertEquals(6, result.getMonthNumber());
    }

    @Test
    void testGetByTrainerIdAndYearAndMonthNumber() {
        when(trainingMonthSummaryRepository.findByTrainerIdAndTrainingYearAndMonthNumber(trainerId, year, monthNumber))
                .thenReturn(Optional.of(trainingMonthSummary));

        TrainingMonthSummary result = trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(trainerId, year, monthNumber);

        assertNotNull(result);
        assertEquals(monthNumber, result.getMonthNumber());
    }

    @Test
    void testGetByTrainerIdAndYearAndMonthNumberWhenResultNotFound() {
        when(trainingMonthSummaryRepository.findByTrainerIdAndTrainingYearAndMonthNumber(trainerId, year, monthNumber))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(trainerId, year, monthNumber));
    }
}
