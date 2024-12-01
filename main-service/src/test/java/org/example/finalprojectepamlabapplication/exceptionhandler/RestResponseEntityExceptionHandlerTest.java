package org.example.finalprojectepamlabapplication.exceptionhandler;

import org.example.finalprojectepamlabapplication.errorhandler.RestResponseEntityExceptionHandler;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new RestResponseEntityExceptionHandler();
    }

    @Test
    public void testHandleEntityNotFoundException() {
        String errorMessage = "Entity not found";
        NoSuchElementException exception = new NoSuchElementException(errorMessage);

        String response = exceptionHandler.handleEntityNotFoundException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.NOT_FOUND);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHandleValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter methodParameter = mock(MethodParameter.class);

        FieldError fieldError = new FieldError("object", "field", "Field error message");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        String response = exceptionHandler.handleValidationException(exception);

        StringBuilder expectedResponse = new StringBuilder();
        expectedResponse.append("field : Field error message")
                .append(System.lineSeparator())
                .append("HttpStatus : ")
                .append("400 BAD_REQUEST");

        assertEquals(expectedResponse.toString(), response);
    }

    @Test
    public void testHandleDataIntegrityViolationException() {
        String errorMessage = "Data integrity violation";
        DataIntegrityViolationException exception = new DataIntegrityViolationException(errorMessage);

        String response = exceptionHandler.handleDataIntegrityViolationException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.BAD_REQUEST);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHandleUnauthorizedException() {
        String errorMessage = "Unauthorized access";
        UnauthorizedException exception = new UnauthorizedException(errorMessage);

        String response = exceptionHandler.handleUnauthorizedException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.UNAUTHORIZED);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHandleUserAlreadyExistException() {
        String errorMessage = "User already exists";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        String response = exceptionHandler.handleUserAlreadyExistException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.BAD_REQUEST);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHandleTrainerWorkloadException() {
        String errorMessage = "Trainer workload exception";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        String response = exceptionHandler.handleTrainerWorkloadException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.BAD_REQUEST);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHandleTrainerWorkloadTimeoutException() {
        String errorMessage = "Trainer workload timeout exception";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        String response = exceptionHandler.handleTrainerWorkloadTimeoutException(exception);
        String expectedResponse = expectedResult(errorMessage, HttpStatus.GATEWAY_TIMEOUT);

        assertEquals(expectedResponse, response);
    }

    private String expectedResult(String message, HttpStatus status){
        StringBuilder expectedResponse = new StringBuilder();
        return expectedResponse
                .append(message)
                .append(System.lineSeparator())
                .append("Http Status : ")
                .append(status)
                .toString();
    }
}
