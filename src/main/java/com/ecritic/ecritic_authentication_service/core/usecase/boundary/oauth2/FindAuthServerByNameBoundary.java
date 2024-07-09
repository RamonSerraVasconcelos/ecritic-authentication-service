package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;

import java.util.Optional;

public interface FindAuthServerByNameBoundary {

    Optional<AuthorizationServer> execute(String name);
}
