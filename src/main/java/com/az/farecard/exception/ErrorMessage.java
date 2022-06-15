package com.az.farecard.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(builderMethodName = "of")
public class ErrorMessage {

    private LocalDateTime localDateTime;
    private String message;
    private String detail;
    private String path;
}