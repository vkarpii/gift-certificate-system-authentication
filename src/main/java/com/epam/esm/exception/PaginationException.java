package com.epam.esm.exception;

public class PaginationException extends RuntimeException {
    public PaginationException(String errorMessage) {
        super(errorMessage);
    }
}
