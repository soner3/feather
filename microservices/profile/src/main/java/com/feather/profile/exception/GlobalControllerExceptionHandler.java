package com.feather.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.feather.lib.util.HttpErrorInfo;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return HttpErrorInfo.methodArgumentInvalidErrorInfo(ex, "Invalid Method Arguments", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> restClientExceptionHandler(RestClientException ex) {

        if (ex instanceof HttpClientErrorException) {
            ProblemDetail problemDetail = ((HttpClientErrorException) ex).getResponseBodyAs(ProblemDetail.class);
            return ResponseEntity.of(problemDetail).build();
        }

        return HttpErrorInfo.errorInfo(ex, "Rest Client Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
