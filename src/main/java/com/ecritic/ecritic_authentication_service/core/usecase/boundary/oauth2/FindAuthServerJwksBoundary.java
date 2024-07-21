package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.nimbusds.jose.jwk.JWKSet;

import java.net.URI;

public interface FindAuthServerJwksBoundary {

    JWKSet execute(URI jwksUri);
}
