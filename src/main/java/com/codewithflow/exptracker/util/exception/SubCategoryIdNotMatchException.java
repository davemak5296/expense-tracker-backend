package com.codewithflow.exptracker.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SubCategoryIdNotMatchException extends RuntimeException {
    public SubCategoryIdNotMatchException(String message) {
        super(message);
    }
}
