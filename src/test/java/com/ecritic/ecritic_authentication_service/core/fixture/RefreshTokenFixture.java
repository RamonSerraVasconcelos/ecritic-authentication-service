package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class RefreshTokenFixture {

    public static RefreshToken load() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setUser(UserFixture.load());
        refreshToken.setActive(true);
        refreshToken.setIssuer("ecritic");
        refreshToken.setAud(Set.of("ecritic"));
        refreshToken.setIssuedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusYears(100));
        return refreshToken;
    }
}
