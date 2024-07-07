package com.ecritic.ecritic_authentication_service.dataprovider.api.fixture;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;

import java.util.UUID;

public class UserEntityResponseFixture {

    public static UserEntityResponse load() {
        return UserEntityResponse.builder()
                .id(UUID.randomUUID().toString())
                .email("email@email.com")
                .password("password")
                .role("DEFAULT")
                .active(true)
                .build();
    }
}
