package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthorizationServerFixture {

    public static AuthorizationServer load() {
        return AuthorizationServer.builder()
                .clientId("asikujdn9872hd892du2")
                .clientSecret("1298e98@*f39#")
                .name("google")
                .responseType("code")
                .authEndpoint(URI.create("https://accounts.google.com/o/oauth2/v2/auth"))
                .tokenEndpoint(URI.create("https://oauth2.googleapis.com/token"))
                .jwksEndpoint(URI.create("https://www.googleapis.com/oauth2/v3/certs"))
                .redirectUris(Set.of(URI.create("http://localhost:8080/oauth2/callback")))
                .scopes(List.of("openid", "profile", "email", "address", "phone"))
                .additionalParams(Map.of("access_type", "offline"))
                .build();
    }
}
