package com.ecritic.ecritic_authentication_service.core.usecase.boundary;

import com.ecritic.ecritic_authentication_service.core.model.User;

import java.util.Optional;

public interface FindUserByEmailBoundary {

    Optional<User> execute(String email);
}
