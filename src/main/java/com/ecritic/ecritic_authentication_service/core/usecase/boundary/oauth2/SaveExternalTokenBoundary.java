package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;

public interface SaveExternalTokenBoundary {

    ExternalToken execute(ExternalToken externalToken);
}
