package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import java.util.UUID;

public interface CheckBlacklistedUserBoundary {

    boolean isUserBlacklisted(UUID userId);
}
