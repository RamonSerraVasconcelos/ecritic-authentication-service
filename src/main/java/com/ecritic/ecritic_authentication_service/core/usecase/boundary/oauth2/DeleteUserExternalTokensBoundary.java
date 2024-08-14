package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import java.util.UUID;

public interface DeleteUserExternalTokensBoundary {

    void execute(UUID userId, String clientId, UUID currentTokenId);
}
