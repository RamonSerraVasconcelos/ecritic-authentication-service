package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.Token;

public interface GenerateUnsignedJwtTokenBoundary {

    String execute(Token token);
}
