package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import java.util.UUID;

public interface BlacklistUserBoundary {

    void execute(UUID userId, int expirationDateTimeInSec);
}
