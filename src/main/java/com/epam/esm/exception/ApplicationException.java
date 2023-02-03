package com.epam.esm.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String errorMessage) {
        super(errorMessage);
    }
}
