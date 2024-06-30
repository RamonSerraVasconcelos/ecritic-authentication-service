package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.Token;

import javax.crypto.SecretKey;

public interface GenerateJwtTokenBoundary {

    String execute(Token token, SecretKey secretKey);
}
