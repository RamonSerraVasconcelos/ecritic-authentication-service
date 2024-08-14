package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;

public interface CacheStateBoundary {

    void execute(String state, AuthorizationRequest authorizationRequest);
}
