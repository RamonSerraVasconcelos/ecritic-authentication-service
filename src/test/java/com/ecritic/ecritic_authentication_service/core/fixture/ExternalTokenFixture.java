package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;

import java.util.UUID;

public class ExternalTokenFixture {

    public static ExternalToken load() {
        return ExternalToken.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .tokenType("Bearer")
                .accessToken("sdasidnasdasd-asdnasikd22323")
                .refreshToken("822983dn9823dh9ed-dsdasdubasid")
                .scope("openid profile email")
                .expiresIn(3600L)
                .idToken("eya9sdha908d928ddasdasdao0sihjd982yud982yd9812hd98ashd98asd9as8dha9s8das98dhas98dh")
                .build();

    }
}
