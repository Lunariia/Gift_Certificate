package com.epam.esm.api.exception;

import com.epam.esm.api.exception.model.ApiError;
import com.epam.esm.exception.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class ServiceExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ServiceExceptionHandler.class);

    private static final Integer UNHANDLED_EXCEPTION_CODE = 50000;
    private static final Integer ILLEGAL_ARGUMENT_EXCEPTION_CODE = 50001;
    private static final Integer ENTITY_NOT_FOUND_EXCEPTION_CODE = 40401;

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(EntityNotFoundException exception, WebRequest request) {
        LOGGER.debug(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                exception.getMessage(),
                ENTITY_NOT_FOUND_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleConflict(IllegalArgumentException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                exception.getMessage(),
                ILLEGAL_ARGUMENT_EXCEPTION_CODE
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);

        String message = "Something going wrong. We are already working on a solution!";
        ApiError apiError = new ApiError(message, UNHANDLED_EXCEPTION_CODE);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
