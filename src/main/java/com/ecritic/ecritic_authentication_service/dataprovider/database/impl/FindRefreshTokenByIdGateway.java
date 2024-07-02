package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindRefreshTokenByIdBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.RefreshTokenEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindRefreshTokenByIdGateway implements FindRefreshTokenByIdBoundary {

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    private final RefreshTokenEntityMapper refreshTokenEntityMapper;

    public Optional<RefreshToken> execute(UUID id) {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenEntityRepository.findById(id.toString());

        return refreshTokenEntity.map(refreshTokenEntityMapper::refreshTokenEntityToRefreshToken);
    }
}
