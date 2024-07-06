package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.Token;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class TokenFixture {

    public static Token load() {
        return Token.builder()
                .id(UUID.randomUUID())
                .user(UserFixture.load())
                .issuer("ecritic")
                .aud(Set.of("ecritic"))
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusYears(100))
                .build();
    }
}
