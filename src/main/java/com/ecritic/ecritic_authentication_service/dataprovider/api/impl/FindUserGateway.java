package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.FindUserBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.UserClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindUserGateway implements FindUserBoundary {

    private final UserClient userClient;

    private final UserEntityMapper userEntityMapper;

    public Optional<User> execute(String email, UUID userId) {
        log.info("Retrieving user authorization info for email [{}] or userId: [{}]", email, userId);

        try {
            String userIdStr = nonNull(userId) ? userId.toString() : null;

            UserEntityResponse userEntityResponse = userClient.getUserAuthorizationInfo(email, userIdStr);

            return Optional.of(userEntityMapper.userEntityToUser(userEntityResponse));
        } catch (Exception ex) {
            log.error("Error retrieving user authorization info", ex);
            return Optional.empty();
        }
    }
}
