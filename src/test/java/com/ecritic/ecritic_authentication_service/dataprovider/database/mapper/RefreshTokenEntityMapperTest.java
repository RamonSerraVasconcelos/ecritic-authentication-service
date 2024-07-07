package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RoleEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.fixture.RefreshTokenEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenEntityMapperTest {

    private RefreshTokenEntityMapper refreshTokenEntityMapper;

    @BeforeEach
    public void setUp() {
        refreshTokenEntityMapper = new RefreshTokenEntityMapper();
    }

    @Test
    void givenRefreshToken_thenReturnRefreshTokenEntity() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityMapper.refreshTokenToRefreshTokenEntity(refreshToken);

        assertThat(refreshTokenEntity).isNotNull();
        assertThat(refreshTokenEntity.getId()).isEqualTo(refreshToken.getId().toString());
        assertThat(refreshTokenEntity.getIssuer()).isEqualTo(refreshToken.getIssuer());
        assertThat(refreshTokenEntity.getAud()).isEqualTo(refreshToken.getAud());
        assertThat(refreshTokenEntity.getUserId()).isEqualTo(refreshToken.getUser().getId().toString());
        assertThat(refreshTokenEntity.getUserRole()).isEqualTo(RoleEntity.valueOf(refreshToken.getUser().getRole().name()));
        assertThat(refreshTokenEntity.isActive()).isEqualTo(refreshToken.isActive());
        assertThat(refreshTokenEntity.getIssuedAt()).isEqualTo(refreshToken.getIssuedAt());
        assertThat(refreshTokenEntity.getExpiresAt()).isEqualTo(refreshToken.getExpiresAt());
    }

    @Test
    void givenRefreshTokenEntity_thenReturnRefreshToken() {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntityFixture.load();

        RefreshToken refreshToken = refreshTokenEntityMapper.refreshTokenEntityToRefreshToken(refreshTokenEntity);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getId().toString()).isEqualTo(refreshTokenEntity.getId());
        assertThat(refreshToken.getIssuer()).isEqualTo(refreshTokenEntity.getIssuer());
        assertThat(refreshToken.getAud()).isEqualTo(refreshTokenEntity.getAud());
        assertThat(refreshToken.getUser().getId().toString()).isEqualTo(refreshTokenEntity.getUserId());
        assertThat(refreshToken.getUser().getRole().name()).isEqualTo(refreshTokenEntity.getUserRole().name());
        assertThat(refreshToken.isActive()).isEqualTo(refreshTokenEntity.isActive());
        assertThat(refreshToken.getIssuedAt()).isEqualTo(refreshTokenEntity.getIssuedAt());
        assertThat(refreshToken.getExpiresAt()).isEqualTo(refreshTokenEntity.getExpiresAt());
    }
}