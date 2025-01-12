package com.client.service_client.controller.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.client.service_client.model.response.ResponseWithInfo;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = "Invalid request body";
        return new ResponseEntity<>(new ResponseWithInfo(errorMessage, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Map<String, String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(new ResponseWithInfo("HTTP method not supported", e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServelt (MissingServletRequestPartException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Required part of the request is missing", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultiparException (MultipartException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Required part of the request is missing", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleNotSupport (HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Unsupported media type in file", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public ResponseEntity<?> handleArgumentConversion (MethodArgumentConversionNotSupportedException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Required conversion in data", e.getMessage()), HttpStatus.GONE);
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<?> handleRollEntity (UnexpectedRollbackException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Unexpected error", e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleValidationException (HandlerMethodValidationException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Bad request", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSqlException(SQLException e) {
        return new ResponseEntity<>(new ResponseWithInfo("Error when the transaction was made", e.getMessage()), HttpStatus.BAD_GATEWAY);
    }
}   
