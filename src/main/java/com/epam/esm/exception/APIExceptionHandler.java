package com.epam.esm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Locale;
import java.util.Objects;

import static com.epam.esm.exception.ExceptionMessage.*;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {
    private final MessageSource messageSource;

    private final int STATUS_CODE_LENGTH = 3;

    @Autowired
    public APIExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<?> handleException(ApplicationException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getMessage(), new Object[]{}, locale);
        APIException apiException = new APIException(message, exception.getMessage());
        int responseStatusCode = getResponseStatusCode(apiException);
        return new ResponseEntity<>(apiException, HttpStatusCode.valueOf(responseStatusCode));
    }

    @ExceptionHandler(value = {PaginationException.class})
    public ResponseEntity<?> handlePaginationException(PaginationException exception, Locale locale) {
        String message = messageSource.getMessage(exception.getMessage(), new Object[]{}, locale);
        log.error(message);
        APIException apiException = new APIException(message, exception.getMessage());
        int responseStatusCode = getResponseStatusCode(apiException);
        return new ResponseEntity<>(apiException, HttpStatusCode.valueOf(responseStatusCode));
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception,Locale locale){
        String errorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String message = messageSource.getMessage(Objects.requireNonNull(errorMessage), new Object[]{}, locale);
        log.error(message);
        APIException apiException = new APIException(message, errorMessage);
        int responseStatusCode = getResponseStatusCode(apiException);
        return new ResponseEntity<>(apiException, HttpStatusCode.valueOf(responseStatusCode));
    }

    private int getResponseStatusCode(APIException apiException){
        return Integer.parseInt(apiException.getErrorCode().substring(0,STATUS_CODE_LENGTH));
    }
}
