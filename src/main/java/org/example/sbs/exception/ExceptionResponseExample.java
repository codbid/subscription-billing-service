package org.example.sbs.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponseExample {
    private String statusCode;
    private String errorMessage;
    private LocalDateTime timestamp;
}
