package com.ecritic.ecritic_authentication_service.dataprovider.database.fixture;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RoleEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class RefreshTokenEntityFixture {

    public static RefreshTokenEntity load() {
        return RefreshTokenEntity.builder()
                .id(UUID.randomUUID().toString())
                .issuer("ecritic")
                .aud(Set.of("ecritic"))
                .active(true)
                .userId(UUID.randomUUID().toString())
                .userRole(RoleEntity.DEFAULT)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
    }
}
