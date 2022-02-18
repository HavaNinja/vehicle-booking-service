package com.hirese.service.exception;

import lombok.Getter;

@Getter
public class HireServiceException extends RuntimeException {
    private final int httpStatus;

    public HireServiceException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
