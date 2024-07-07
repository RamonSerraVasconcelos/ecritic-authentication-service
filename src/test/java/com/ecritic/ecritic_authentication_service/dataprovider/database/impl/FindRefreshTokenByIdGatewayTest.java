package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.fixture.RefreshTokenFixture;
import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.fixture.RefreshTokenEntityFixture;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.RefreshTokenEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRefreshTokenByIdGatewayTest {

    @InjectMocks
    private FindRefreshTokenByIdGateway findRefreshTokenByIdGateway;

    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Mock
    private RefreshTokenEntityMapper refreshTokenEntityMapper;

    @Test
    void givenExecution_thenFind_andReturnRefreshToken() {
        RefreshToken refreshToken = RefreshTokenFixture.load();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntityFixture.load();

        when(refreshTokenEntityRepository.findById(refreshToken.getId().toString())).thenReturn(Optional.of(refreshTokenEntity));
        when(refreshTokenEntityMapper.refreshTokenEntityToRefreshToken(refreshTokenEntity)).thenReturn(refreshToken);

        Optional<RefreshToken> resultToken = findRefreshTokenByIdGateway.execute(refreshToken.getId());

        verify(refreshTokenEntityRepository).findById(refreshToken.getId().toString());
        verify(refreshTokenEntityMapper).refreshTokenEntityToRefreshToken(refreshTokenEntity);

        assertThat(resultToken).isPresent().get().usingRecursiveComparison().isEqualTo(refreshToken);
    }
}