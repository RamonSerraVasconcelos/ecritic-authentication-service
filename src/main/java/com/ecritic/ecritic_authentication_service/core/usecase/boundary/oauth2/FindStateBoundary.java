package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;

import java.util.Optional;

public interface FindStateBoundary {

    Optional<AuthorizationRequest> execute(String state);
}
