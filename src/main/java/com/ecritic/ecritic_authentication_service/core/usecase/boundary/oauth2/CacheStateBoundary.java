package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

public interface CacheStateBoundary {

    void execute(String state);
}
