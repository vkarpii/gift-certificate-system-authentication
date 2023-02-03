package com.epam.esm.exception;

import lombok.Getter;

@Getter
public class APIException {
    private final String errorMessage;
    private final String errorCode;

    public APIException(String errorMessage, String message) {
        this.errorMessage = errorMessage;
        this.errorCode = ExceptionCode.getCodeByMessage(message);
    }

}
