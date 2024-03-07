package com.codewithflow.exptracker.util.exception;

import com.codewithflow.exptracker.response.GenericResponse;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionHandlers {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected GenericResponse<?,?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> Objects.requireNonNull(error.getDefaultMessage()).split(";"))
                .flatMap(Stream::of)
                .toList();
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), errors, request.getDescription(false)));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GenericResponse<?,?> authenticationExceptionHandler(AuthenticationException ex, WebRequest request) {
        if (ex instanceof BadCredentialsException) {
            return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList("Invalid password"), request.getDescription(false)));
        } else if (ex instanceof DisabledException) {
            return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList("User is disabled. Please check your mailbox for activation link"), request.getDescription(false)));
        }
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));

    }

    @ExceptionHandler(JwtException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<?,?> jwtExceptionHandler(JwtException ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));
    }

    @ExceptionHandler(MailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<?,?> mailExceptionHandler(MailException ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList("Mail error: " + ex.getMessage()), request.getDescription(false)));
    }

    @ExceptionHandler(SubCategoryIdNotMatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<?,?> subCategoryIdNotMatchExceptionHandler(SubCategoryIdNotMatchException ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<?,?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new GenericResponse<>(false, null, new ErrorDetails(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false)));
    }
}
