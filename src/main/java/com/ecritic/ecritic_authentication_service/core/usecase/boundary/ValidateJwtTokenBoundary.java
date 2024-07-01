package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.Token;

import javax.crypto.SecretKey;

public interface ValidateJwtTokenBoundary {

    Token execute(String jwtToken, SecretKey secretKey);
}
