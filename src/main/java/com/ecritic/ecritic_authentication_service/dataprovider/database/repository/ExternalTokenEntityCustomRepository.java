package com.ecritic.ecritic_authentication_service.dataprovider.database.repository;

import java.util.UUID;

public interface ExternalTokenEntityCustomRepository {

    void deleteUserTokens(UUID userId, String clientId, UUID currentTokenId);
}
