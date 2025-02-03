package com.feather.lib.util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class HttpErrorInfo {

    public static ResponseEntity<ProblemDetail> errorInfo(Exception ex, String title, HttpStatus httpStatus) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
        String exceptionClassPath = ex.getClass().getName().replace(".", "/");
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(exceptionClassPath));
        return ResponseEntity.of(problemDetail).build();
    }

    public static ResponseEntity<ProblemDetail> methodArgumentInvalidErrorInfo(MethodArgumentNotValidException ex,
            String title, HttpStatus httpStatus) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, "Field validation failed");
        String exceptionClassPath = ex.getClass().getName().replace(".", "/");
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(exceptionClassPath));

        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String fieldErrorMessage = error.getDefaultMessage();
                errors.put(fieldName, fieldErrorMessage);
            }
        });

        problemDetail.setProperties(errors);

        return ResponseEntity.of(problemDetail).build();

    }

}
