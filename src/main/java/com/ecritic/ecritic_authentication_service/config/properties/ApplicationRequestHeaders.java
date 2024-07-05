package com.ecritic.ecritic_authentication_service.config.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ApplicationRequestHeaders {

    REQUEST_ID("X-Request-Id"),
    AUTHORIZATION("Authorization");

    private final String value;
}
