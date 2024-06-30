package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DefaultException extends RuntimeException {

    private ErrorResponse errorResponse;
}
