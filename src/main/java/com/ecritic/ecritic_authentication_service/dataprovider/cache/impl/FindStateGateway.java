package com.ecritic.ecritic_authentication_service.dataprovider.cache.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindStateBoundary;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindStateGateway implements FindStateBoundary {

    private final Jedis jedis;

    private final ObjectMapper objectMapper;

    @Override
    public Optional<AuthorizationRequest> execute(String state) {
        log.info("Finding authorization request info with state: [{}]", state);

        try {
            String authorizationRequestJson = jedis.get(state);

            if (authorizationRequestJson == null) {
                return Optional.empty();
            }

            return Optional.of(objectMapper.readValue(authorizationRequestJson, AuthorizationRequest.class));
        } catch (Exception ex) {
            log.error("Error finding authorization request info with state: [{}]", state, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }
}
