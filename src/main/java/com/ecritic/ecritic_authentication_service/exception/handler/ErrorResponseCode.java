package com.ecritic.ecritic_authentication_service.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorResponseCode {

    ECRITICAUTH_01("ECRITICAUTH-01", "Invalid request data", "Invalid request data"),
    ECRITICAUTH_02("ECRITICAUTH-02", "Missing required headers", "Missing required headers"),
    ECRITICAUTH_03("ECRITICAUTH-03", "Unauthorized access", "Unauthorized access"),
    ECRITICAUTH_04("ECRITICAUTH-04", "Resource not found", "Resource not found"),
    ECRITICAUTH_05("ECRITICAUTH-05", "Resource conflict", "Resource conflict"),
    ECRITICAUTH_06("ECRITICAUTH-06", "Resource violation", "Resource violation"),
    ECRITICAUTH_07("ECRITICAUTH-07", "Unauthorized", "Invalid email or password"),
    ECRITICAUTH_08("ECRITICAUTH-08", "Banned user", "User is currently banned");

    private final String code;
    private final String message;
    private final String detail;
}
