package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.nimbusds.jose.jwk.JWKSet;

public interface ValidateIdTokenBoundary {

    IdToken execute(String idToken, JWKSet jwkSet);
}
