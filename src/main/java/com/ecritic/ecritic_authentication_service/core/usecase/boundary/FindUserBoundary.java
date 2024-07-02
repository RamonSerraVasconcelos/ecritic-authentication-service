package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.User;

import java.util.Optional;
import java.util.UUID;

public interface FindUserBoundary {

    Optional<User> execute(String email, UUID userId);
}
