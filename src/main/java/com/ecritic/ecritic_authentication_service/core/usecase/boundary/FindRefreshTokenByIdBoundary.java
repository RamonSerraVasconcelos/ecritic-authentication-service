package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface FindRefreshTokenByIdBoundary {

    Optional<RefreshToken> execute(UUID id);
}
