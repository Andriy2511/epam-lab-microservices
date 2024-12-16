package org.example.trainerworkloadservice.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.trainerworkloadservice.enums.ActionType;
import org.example.trainerworkloadservice.model.TrainerWorkload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainerWorkloadRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTrainerWorkloadRequestDTO() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "John",
                "Doe",
                true,
                new Date(),
                60,
                ActionType.ADD
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidUsernameBlank() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "",
                "John",
                "Doe",
                true,
                new Date(),
                60,
                ActionType.ADD
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Username cannot be blank")));
    }

    @Test
    void testInvalidFirstNameBlank() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "",
                "Doe",
                true,
                new Date(),
                60,
                ActionType.ADD
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("First name cannot be blank")));
    }

    @Test
    void testInvalidLastNameBlank() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "John",
                "",
                true,
                new Date(),
                60,
                ActionType.DELETE
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Last name cannot be blank")));
    }

    @Test
    void testInvalidDateNull() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "John",
                "Doe",
                true,
                null,
                60,
                ActionType.DELETE
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Training date cannot be null")));
    }

    @Test
    void testInvalidTrainingDurationTooShort() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "John",
                "Doe",
                true,
                new Date(),
                10,
                ActionType.ADD
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Training duration must be at least 1 minute")));
    }

    @Test
    void testInvalidActionTypeNull() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "testuser",
                "John",
                "Doe",
                true,
                new Date(),
                60,
                null
        );

        Set<ConstraintViolation<TrainerWorkloadRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Action type cannot be null")));
    }

    @Test
    void testToTrainerWorkload() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(
                1L,
                "test.user",
                "John",
                "Doe",
                true,
                new Date(),
                60,
                ActionType.ADD
        );

        TrainerWorkload workload = TrainerWorkloadRequestDTO.toTrainerWorkload(dto);

        assertNotNull(workload);
        assertEquals(1L, workload.getTrainerId());
        assertEquals("test.user", workload.getUsername());
        assertEquals("John", workload.getFirstName());
        assertEquals("Doe", workload.getLastName());
        assertTrue(workload.isActive());
    }
}

