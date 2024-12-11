package org.example.trainerworkloadservice.errorhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleEntityNotFoundException(ConstraintViolationException e) {
        errorDetailsBuilder = new StringBuilder();
        return buildStringWithErrorDetailsAndStatus(errorDetailsBuilder, HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(ListenerExecutionFailedException.class)
    public String handleJmsListenerException(ListenerExecutionFailedException e) {
        errorDetailsBuilder = new StringBuilder();
        if(e.getCause() instanceof ConstraintViolationException) {
            return buildStringWithErrorDetailsAndStatus(errorDetailsBuilder, HttpStatus.BAD_REQUEST, e);
        } else {
            return buildStringWithErrorDetailsAndStatus(errorDetailsBuilder, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
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

