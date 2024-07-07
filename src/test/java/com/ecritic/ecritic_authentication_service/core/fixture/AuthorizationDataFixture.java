package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;

public class AuthorizationDataFixture {

    public static AuthorizationData load() {
        return AuthorizationData.builder()
                .accessToken(AccessTokenFixture.load())
                .refreshToken(RefreshTokenFixture.load())
                .build();
    }
}
