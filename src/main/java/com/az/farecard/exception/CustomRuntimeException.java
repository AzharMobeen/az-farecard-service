package com.az.farecard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException{

    private final String message;
    private final String detail;
    private final HttpStatus httpStatus;

    public CustomRuntimeException(String message, String detail, HttpStatus httpStatus){
        super(message);
        this.message = message;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }
}