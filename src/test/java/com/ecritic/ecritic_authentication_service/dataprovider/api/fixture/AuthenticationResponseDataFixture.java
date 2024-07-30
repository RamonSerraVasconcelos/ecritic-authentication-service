package com.ecritic.ecritic_authentication_service.dataprovider.api.fixture;

import com.ecritic.ecritic_authentication_service.dataprovider.jwt.fixture.JwtFixture;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;

public class AuthenticationResponseDataFixture {

    public static AuthenticationResponseData load() {
        return AuthenticationResponseData.builder()
                .accessToken(JwtFixture.loadSignedJwt())
                .refreshToken(JwtFixture.loadSignedJwt())
                .tokenType("Bearer")
                .expiresIn(3600)
                .refreshExpiresIn(7200)
                .build();
    }
}
