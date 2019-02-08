package com.korkeat.mobileregistration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Resource not found")
    @ExceptionHandler({HttpMessageNotWritableException.class})
    public void handle(HttpMessageNotWritableException ex) {}

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Resource not found")
    @ExceptionHandler({EntityNotFoundException.class})
    public void handle(EntityNotFoundException ex) {}

    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Resource already exists")
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public void handle(SQLIntegrityConstraintViolationException ex) {}

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid input")
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public void handle(MethodArgumentTypeMismatchException ex) {}

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Server error")
    @ExceptionHandler({NullPointerException.class, SQLException.class})
    public void handle(Exception ex) {}
}
