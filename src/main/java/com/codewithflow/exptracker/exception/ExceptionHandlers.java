package com.codewithflow.exptracker.exception;

import com.codewithflow.exptracker.response.GenericResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    private static final String MESSAGE_DUPLICATE_EMAIL = "Email already exists";

    private static final String MESSAGE_DATA_ERROR_DEFAULT = "Invalid input. Please check your data and try again.";

    private static final Map<String, String> CONSTRAINT_ERRMSG_MAP = Map.of(
            "users_email_key", MESSAGE_DUPLICATE_EMAIL
    );

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericResponse<?,?> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<?,?> constraintViolationExceptionHandler(ConstraintViolationException ex, WebRequest request) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(error -> String.format(
                        "%s.%s - Provided: %s. Error msg: %s",
                        error.getRootBeanClass().getSimpleName(),
                        error.getPropertyPath(),
                        error.getInvalidValue(),
                        error.getMessage()))
                .toList();

        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), errors, request.getDescription(false)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<?,?> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex, WebRequest request) {
        logger.error(ex.getMessage());

        String rootMsg = ex.getMostSpecificCause().getMessage();

        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry: CONSTRAINT_ERRMSG_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(entry.getValue()), request.getDescription(false)));
                }
            }
        }

        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(MESSAGE_DATA_ERROR_DEFAULT), request.getDescription(false)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format(
                        "%s - Provided: %s. Error msg: %s",
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .toList();

        ErrorDetails errorDetails = new ErrorDetails(new Date(), errors, request.getDescription(false));
        return new ResponseEntity<>(new GenericResponse<>(false, null, errorDetails), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<?,?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));
    }
}
