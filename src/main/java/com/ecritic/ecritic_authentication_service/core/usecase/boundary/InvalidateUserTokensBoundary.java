package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import java.util.UUID;

public interface InvalidateUserTokensBoundary {

    void execute(UUID userId);
}
