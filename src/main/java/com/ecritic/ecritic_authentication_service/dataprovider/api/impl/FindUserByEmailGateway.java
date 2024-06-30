package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserByEmailBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.UserClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindUserByEmailGateway implements FindUserByEmailBoundary {

    private final UserClient userClient;

    private final UserEntityMapper userEntityMapper;

    public Optional<User> execute(String email) {
        log.info("Retrieving user authorization info for email [{}]", email);

        try {
            UserEntity userEntity = userClient.getUserAuthorizationInfo(email);

            return Optional.of(userEntityMapper.userEntityToUser(userEntity));
        } catch (Exception ex) {
            log.error("Error retrieving user authorization info", ex);
            return Optional.empty();
        }
    }
}
