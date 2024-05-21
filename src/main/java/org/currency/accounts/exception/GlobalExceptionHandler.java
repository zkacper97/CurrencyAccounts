package org.currency.accounts.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.currency.accounts.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return createErrorResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = {
            InsufficientFundsException.class,
            CurrencyExchangeFailedException.class,
            SameCurrencyExchangeException.class,
            PeselValidationException.class,
            UserAlreadyExistsException.class,
            UserAgeValidationException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException ex, WebRequest request) {
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private static ResponseEntity<ErrorResponse> createErrorResponseEntity(HttpStatus internalServerError, Exception ex) {
        log.error("Error occurred: {}  {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(internalServerError.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, internalServerError);

    }
}