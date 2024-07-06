package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class AccessTokenFixture {

    public static AccessToken load() {
        AccessToken accessToken = new AccessToken();
        accessToken.setId(UUID.randomUUID());
        accessToken.setUser(UserFixture.load());
        accessToken.setIssuer("ecritic");
        accessToken.setAud(Set.of("ecritic"));
        accessToken.setIssuedAt(LocalDateTime.now());
        accessToken.setExpiresAt(LocalDateTime.now().plusYears(100));
        return accessToken;
    }
}
