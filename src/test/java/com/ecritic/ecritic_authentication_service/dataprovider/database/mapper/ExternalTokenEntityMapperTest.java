package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.fixture.ExternalTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.fixture.ExternalTokenEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalTokenEntityMapperTest {

    private ExternalTokenEntityMapper externalTokenEntityMapper;

    @BeforeEach
    void setUp() {
        externalTokenEntityMapper = Mappers.getMapper(ExternalTokenEntityMapper.class);
    }

    @Test
    void givenExternalToken_thenReturnExternalTokenEntity() {
        ExternalToken externalToken = ExternalTokenFixture.load();

        ExternalTokenEntity externalTokenEntity = externalTokenEntityMapper.externalTokenToExternalTokenEntity(externalToken);

        assertThat(externalTokenEntity).isNotNull();
        assertThat(externalTokenEntity.getClientId()).isEqualTo(externalToken.getClientId());
        assertThat(externalTokenEntity.getUserId()).isEqualTo(externalToken.getUserId().toString());
        assertThat(externalTokenEntity.getRefreshToken()).isEqualTo(externalToken.getRefreshToken());
        assertThat(externalTokenEntity.getScope()).isEqualTo(externalToken.getScope());
        assertThat(externalTokenEntity.getTokenType()).isEqualTo(externalToken.getTokenType());
    }

    @Test
    void givenExternalTokenEntity_thenReturnExternalToken() {
        ExternalTokenEntity externalTokenEntity = ExternalTokenEntityFixture.load();

        ExternalToken externalToken = externalTokenEntityMapper.externalTokenEntityToExternalToken(externalTokenEntity);

        assertThat(externalToken).isNotNull();
        assertThat(externalToken.getClientId()).isEqualTo(externalTokenEntity.getClientId());
        assertThat(externalToken.getUserId()).isEqualTo(UUID.fromString(externalTokenEntity.getUserId()));
        assertThat(externalToken.getRefreshToken()).isEqualTo(externalTokenEntity.getRefreshToken());
        assertThat(externalToken.getScope()).isEqualTo(externalTokenEntity.getScope());
        assertThat(externalToken.getTokenType()).isEqualTo(externalTokenEntity.getTokenType());
    }
}