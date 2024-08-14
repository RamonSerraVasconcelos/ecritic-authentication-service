package com.ecritic.ecritic_authentication_service.dataprovider.database.fixture;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;

import java.util.UUID;

public class ExternalTokenEntityFixture {

    public static ExternalTokenEntity load() {
        return ExternalTokenEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(UUID.randomUUID().toString())
                .tokenType("Bearer")
                .refreshToken("822983dn9823dh9ed-dsdasdubasid")
                .scope("openid profile email")
                .build();
    }
}
