package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.UpsertUserBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.UserClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserRequest;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.UserEntityMapper;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.ClientException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpsertUserGateway implements UpsertUserBoundary {

    private final UserClient userClient;

    private final UserEntityMapper userEntityMapper;

    public User execute(String email, String name) {
        try {
            UserRequest userRequest = UserRequest.builder()
                    .email(email)
                    .name(name)
                    .build();

            UserEntityResponse userEntityResponse = userClient.upsertUser(userRequest);

            return userEntityMapper.userEntityToUser(userEntityResponse);
        } catch (ClientException ex) {
            log.error("Error while making request to Users Service. Error: [{}]", ex.getClientErrorResponse());
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_13);
        } catch (Exception ex) {
            log.error("Error retrieving upserting user", ex);
            throw  ex;
        }
    }
}
