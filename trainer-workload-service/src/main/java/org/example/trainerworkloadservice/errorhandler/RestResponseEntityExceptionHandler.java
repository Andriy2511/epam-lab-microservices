package org.example.trainerworkloadservice.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private StringBuilder errorDetailsBuilder;

    @ExceptionHandler(NoSuchElementException.class)
    public String handleEntityNotFoundException(NoSuchElementException e) {
        errorDetailsBuilder = new StringBuilder();
        return buildStringWithErrorDetailsAndStatus(errorDetailsBuilder, HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e) {
        errorDetailsBuilder = new StringBuilder();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errorDetailsBuilder.append(error.getField())
                        .append(" : ")
                        .append(error.getDefaultMessage())
                        .append(System.lineSeparator()));

        errorDetailsBuilder
                .append("HttpStatus : ")
                .append(HttpStatus.BAD_REQUEST);
        return errorDetailsBuilder.toString();
    }

    private String buildStringWithErrorDetailsAndStatus(StringBuilder errorDetails, HttpStatus status, Exception e) {
        errorDetails
                .append(e.getMessage())
                .append(System.lineSeparator())
                .append("Http Status : ")
                .append(status);

        return errorDetails.toString();
    }
}

