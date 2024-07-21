package com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.User;

public interface UpsertUserBoundary {

    User execute(String email, String name);
}
