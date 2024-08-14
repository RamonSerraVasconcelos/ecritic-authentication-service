package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;

public class InternalErrorException extends DefaultException {

    public InternalErrorException(ErrorResponseCode errorResponseCode) {
        super(ErrorResponse.builder()
                .code(errorResponseCode.getCode())
                .message(errorResponseCode.getMessage())
                .detail(errorResponseCode.getDetail())
                .build());
    }
}
