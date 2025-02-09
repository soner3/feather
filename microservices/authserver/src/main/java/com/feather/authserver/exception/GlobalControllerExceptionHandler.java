package com.feather.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.feather.lib.exception.AlreadyExistsException;
import com.feather.lib.util.HttpErrorInfo;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return HttpErrorInfo.methodArgumentInvalidErrorInfo(ex, "Invalid Method Arguments", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> alreadyExistsExceptionHandler(AlreadyExistsException ex) {
        return HttpErrorInfo.errorInfo(ex, "Already Exists", HttpStatus.BAD_REQUEST);

    }

}
