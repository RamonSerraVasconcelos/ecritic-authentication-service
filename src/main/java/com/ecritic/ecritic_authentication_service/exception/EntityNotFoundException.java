package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;

public class EntityNotFoundException extends DefaultException {

    public EntityNotFoundException(ErrorResponseCode errorResponseCode) {
        super(ErrorResponse.builder()
                .code(errorResponseCode.getCode())
                .message(errorResponseCode.getMessage())
                .detail(errorResponseCode.getDetail())
                .build());
    }
}
