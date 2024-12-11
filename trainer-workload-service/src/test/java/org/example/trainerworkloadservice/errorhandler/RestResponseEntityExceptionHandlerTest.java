package org.example.trainerworkloadservice.errorhandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
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
