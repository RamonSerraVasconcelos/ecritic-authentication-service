package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.model.enums.Role;

import java.util.UUID;

public class UserFixture {

    public static User load() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("email@email.com")
                .password("password")
                .role(Role.DEFAULT)
                .active(true)
                .build();
    }
}
