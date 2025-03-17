package com.feather.authserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.feather.lib.exception.AlreadyExistsException;
import com.feather.lib.util.HttpErrorInfo;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex) {
        return HttpErrorInfo.methodArgumentInvalidErrorInfo(ex, "Invalid Method Arguments", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleAlreadyExistsExceptionHandler(AlreadyExistsException ex) {
        return HttpErrorInfo.errorInfo(ex, "Already Exists", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException ex) {
        return HttpErrorInfo.errorInfo(ex, "Bad Credentials", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleJwtException(JwtException ex) {
        return HttpErrorInfo.errorInfo(ex, "Jwt Exception", HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleBadJwtException(BadJwtException ex) {
        return HttpErrorInfo.errorInfo(ex, "Unknown Token Issuer", HttpStatus.UNAUTHORIZED);

    }

}
