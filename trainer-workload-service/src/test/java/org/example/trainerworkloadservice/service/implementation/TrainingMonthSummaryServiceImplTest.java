package org.example.trainerworkloadservice.service.implementation;

import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.example.trainerworkloadservice.repository.TrainingMonthSummaryCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingMonthSummaryServiceImplTest {

    private Long trainerId;
    private Integer year;
    private Integer monthNumber;

    private TrainingMonthSummary trainingMonthSummary;

    @Mock
    private TrainingMonthSummaryCustomRepository trainingMonthSummaryCustomRepository;

    @InjectMocks
    private TrainingMonthSummaryServiceImpl trainingMonthSummaryService;

    @BeforeEach
    void setUp() {
        trainerId = 100L;
        year = 2023;
        monthNumber = 6;

        trainingMonthSummary = new TrainingMonthSummary();
        trainingMonthSummary.setMonthNumber(monthNumber);
    }

    @Test
    void testGetByTrainerIdAndYearAndMonthNumber() {
        when(trainingMonthSummaryCustomRepository.findTrainingMonthSummary(trainerId, year, monthNumber))
                .thenReturn(Optional.of(trainingMonthSummary));

        TrainingMonthSummary result = trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(trainerId, year, monthNumber);
        assertNotNull(result);
        assertEquals(monthNumber, result.getMonthNumber());
    }

    @Test
    void testGetMonthByMonthNumberAndTrainingYearWhenResultNotFound() {
        when(trainingMonthSummaryCustomRepository.findTrainingMonthSummary(trainerId, year, monthNumber))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> trainingMonthSummaryService.getByTrainerIdAndYearAndMonthNumber(trainerId, year, monthNumber));
    }
}
