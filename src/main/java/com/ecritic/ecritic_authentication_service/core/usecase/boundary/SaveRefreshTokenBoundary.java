package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;

public interface SaveRefreshTokenBoundary {

    RefreshToken execute(RefreshToken refreshToken);
}
