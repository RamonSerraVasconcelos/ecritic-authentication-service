package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;

public interface GenerateExternalTokenBoundary {

    ExternalToken execute(AuthorizationRequest authorizationRequest, AuthorizationServer authorizationServer, String code);
}
