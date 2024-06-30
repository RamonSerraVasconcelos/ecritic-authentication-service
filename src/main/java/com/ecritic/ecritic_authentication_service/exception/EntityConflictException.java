package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;

public class EntityConflictException extends DefaultException {

    public EntityConflictException(ErrorResponseCode errorResponseCode) {
        super(ErrorResponse.builder()
                .code(errorResponseCode.getCode())
                .message(errorResponseCode.getMessage())
                .detail(errorResponseCode.getDetail())
                .build());
    }
}
