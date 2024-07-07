package com.ecritic.ecritic_authentication_service.dataprovider.api.fixture;

import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;

public class AuthorizationResponseDataFixture {

    public static AuthorizationResponseData load() {
        return AuthorizationResponseData.builder()
                .accessToken(JwtFixture.loadSignedJwt())
                .refreshToken(JwtFixture.loadSignedJwt())
                .tokenType("Bearer")
                .expiresIn(3600)
                .refreshExpiresIn(7200)
                .build();
    }
}
