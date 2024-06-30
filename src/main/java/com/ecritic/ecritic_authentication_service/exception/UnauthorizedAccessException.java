package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;

public class UnauthorizedAccessException extends DefaultException {

    public UnauthorizedAccessException(ErrorResponseCode errorResponseCode) {
        super(ErrorResponse.builder()
                .code(errorResponseCode.getCode())
                .message(errorResponseCode.getMessage())
                .detail(errorResponseCode.getDetail())
                .build());
    }

    public UnauthorizedAccessException(ErrorResponseCode errorResponseCode, String exceptionDetail) {
        super(ErrorResponse.builder()
                .code(errorResponseCode.getCode())
                .message(errorResponseCode.getMessage())
                .detail(exceptionDetail)
                .build());
    }
}
