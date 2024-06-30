package com.ecritic.ecritic_authentication_service.exception.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ErrorResponse {

    private String code;
    private String message;
    private String detail;
}
