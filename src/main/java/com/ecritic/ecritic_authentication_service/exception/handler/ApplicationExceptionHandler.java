package com.ecritic.ecritic_authentication_service.exception.handler;

import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.EntityConflictException;
import com.ecritic.ecritic_authentication_service.exception.EntityNotFoundException;
import com.ecritic.ecritic_authentication_service.exception.ResourceViolationException;
import com.ecritic.ecritic_authentication_service.exception.UnauthorizedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    private ErrorResponse buildResponseError(ErrorResponseCode errorCode, String detail) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .detail(detail)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("Request INFO - Response returning violations found: [{}]", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_01, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("Request INFO - Response returning violations found: [{}]", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_01, ex.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> missingRequestHeaderExceptionHandler(MissingRequestHeaderException ex) {
        log.warn("Request INFO - Response returning violations found: [{}]", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_02, ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("Request INFO - Response returning violations found: [{}]", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_01, ex.getMessage()));
    }

    @ExceptionHandler(ResourceViolationException.class)
    public ResponseEntity<ErrorResponse> resourceViolationExceptionHandler(ResourceViolationException ex) {
        log.warn("Request INFO - Response returning violations found: [{}]", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_06, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> unauthorizedAccessExceptionHandler(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getErrorResponse());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> fileSizeLimitExceededExceptionHandler(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_01, ex.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> multipartExceptionHandler(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ErrorResponseCode.ECRITICUSERS_01, ex.getMessage()));
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ErrorResponse> resourceConflictExceptionHandler(EntityConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getErrorResponse());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorResponse());
    }

    @ExceptionHandler(BusinessViolationException.class)
    public ResponseEntity<ErrorResponse> businessViolationExceptionHandler(BusinessViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorResponse());
    }
}
