package com.az.farecard.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {CustomRuntimeException.class})
    public ResponseEntity<ErrorMessage> handleCustomRuntimeException(CustomRuntimeException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(exception.getMessage(), request, exception.getDetail());
        return new ResponseEntity<>(errorMessage, exception.getHttpStatus());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(exception.getMessage(), request,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessage>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            ErrorMessage errorMessage = buildErrorMessage(fieldName, request, error.getDefaultMessage());
            errorMessages.add(errorMessage);
        });
        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(EntityNotFoundException ex, WebRequest request) {

        ErrorMessage errorMessage = buildErrorMessage(ex.getMessage(), request,
                HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    private ErrorMessage buildErrorMessage(String exception, WebRequest request, String detail){
        String path = request.getDescription(false).split("=")[1];
        return ErrorMessage.of().localDateTime(LocalDateTime.now())
                .message(exception)
                .path(path)
                .detail(detail)
                .build();
    }
}