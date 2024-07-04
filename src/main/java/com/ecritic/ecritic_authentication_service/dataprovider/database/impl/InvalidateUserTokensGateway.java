package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.InvalidateUserTokensBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InvalidateUserTokensGateway implements InvalidateUserTokensBoundary {

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    public void execute(UUID userId) {
        refreshTokenEntityRepository.invalidateByUserId(userId);
    }
}
