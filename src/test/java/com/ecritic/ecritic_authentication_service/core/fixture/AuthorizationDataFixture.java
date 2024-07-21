package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;

public class AuthorizationDataFixture {

    public static AuthenticationData load() {
        return AuthenticationData.builder()
                .accessToken(AccessTokenFixture.load())
                .refreshToken(RefreshTokenFixture.load())
                .build();
    }
}
