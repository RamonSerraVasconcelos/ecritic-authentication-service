package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;

import java.net.URI;

public class AuthorizationRequestFixture {

    public static AuthorizationRequest load() {
        return AuthorizationRequest.builder()
                .clientId("aosuidasd89232093jn")
                .redirectUri(URI.create("http://localhost:8080/oauth2/callback"))
                .build();
    }
}
