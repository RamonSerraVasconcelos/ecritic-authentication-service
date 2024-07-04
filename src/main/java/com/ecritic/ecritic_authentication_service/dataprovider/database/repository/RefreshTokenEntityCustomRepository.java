package com.ecritic.ecritic_authentication_service.dataprovider.database.repository;

import java.util.UUID;

public interface RefreshTokenEntityCustomRepository {

    void invalidateByUserId(UUID userId);
}
