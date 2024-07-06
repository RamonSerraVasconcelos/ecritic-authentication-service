package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class RefreshTokenFixture {

    private static final String JWT_TOKEN = "eyJhbGciOiJub25lIn0.eyJqdGkiOiI5M2IzMjIxOS0xYWU5LTQ0NTQtYTBhNC01ZjU2OTU2NGM2c2QiLCJ1c2VySWQiOiIwYzVkODM4OC03ZmU1LTRmNGUtYTNmMS05ZWFhMDgzZDJkNzkiLCJ1c2VyUm9sZSI6IkRFRkFVTFQiLCJpYXQiOjk5MTgzMzYyMzksImV4cCI6OTkxODMzNjIzOX0.";

    public static RefreshToken load() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setJwt(JWT_TOKEN);
        refreshToken.setActive(true);
        refreshToken.setIssuer("ecritic");
        refreshToken.setAud(Set.of("ecritic"));
        refreshToken.setIssuedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        return refreshToken;
    }
}
