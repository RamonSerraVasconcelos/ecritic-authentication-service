package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.SaveRefreshTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.RefreshTokenEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRefreshTokenGateway implements SaveRefreshTokenBoundary {

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    private final RefreshTokenEntityMapper refreshTokenEntityMapper;

    @Override
    public RefreshToken execute(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityMapper.refreshTokenToRefreshTokenEntity(refreshToken);

        refreshTokenEntityRepository.save(refreshTokenEntity);

        return refreshTokenEntityMapper.refreshTokenEntityToRefreshToken(refreshTokenEntity);
    }
}
